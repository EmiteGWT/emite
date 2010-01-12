package com.calclab.emite.core.client.xmpp.sasl;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.Packet;
import com.calclab.emite.xtesting.ConnectionTester;
import com.calclab.suco.testing.events.MockedListener;

public class SASLManagerTest {
    private SASLManager manager;
    private MockedListener<AuthorizationTransaction> listener;
    private ConnectionTester connection;

    @Before
    public void beforeTests() {
	connection = new ConnectionTester();
	manager = new SASLManager(connection);
	listener = new MockedListener<AuthorizationTransaction>();
	manager.onAuthorized(listener);
    }

    @Test
    public void shouldHandleSuccessWhenAuthorizationSent() {
	manager.sendAuthorizationRequest(new AuthorizationTransaction(uri("me@domain"), "password"));
	connection.receives("<success xmlns=\"urn:ietf:params:xml:ns:xmpp-sasl\"/>");
	assertTrue(listener.isCalledOnce());
	assertSame(AuthorizationTransaction.State.succeed, listener.getValue(0).getState());
    }

    @Test
    public void shouldHanonStanzadleFailure() {
	manager.sendAuthorizationRequest(new AuthorizationTransaction(uri("node@domain"), "password"));
	connection.receives("<failure xmlns=\"urn:ietf:params:xml:ns:xmpp-sasl\"><not-authorized/></failure>");
	assertTrue(listener.isCalledOnce());
	assertSame(AuthorizationTransaction.State.failed, listener.getValue(0).getState());
    }

    @Test
    public void shouldSendAnonymousIfAnonymousProvided() {
	manager.sendAuthorizationRequest(new AuthorizationTransaction(SASLManager.ANONYMOUS, null));
	IPacket packet = new Packet("auth", "urn:ietf:params:xml:ns:xmpp-sasl").With("mechanism", "ANONYMOUS");
	assertTrue(connection.hasSent(packet));
    }

    @Test
    public void shouldSendPlainAuthorizationUnlessAnonymous() {
	manager.sendAuthorizationRequest(new AuthorizationTransaction(uri("node@domain/resource"), "password"));
	IPacket packet = new Packet("auth", "urn:ietf:params:xml:ns:xmpp-sasl").With("mechanism", "PLAIN");
	assertTrue(connection.hasSent(packet));
    }

    @Test
    public void shouldSendPlainAuthorizationWithoutNode() {
	manager.sendAuthorizationRequest(new AuthorizationTransaction(uri("domain/resource"), null));
	IPacket packet = new Packet("auth", "urn:ietf:params:xml:ns:xmpp-sasl").With("mechanism", "PLAIN");
	assertTrue(connection.hasSent(packet));
    }

}
