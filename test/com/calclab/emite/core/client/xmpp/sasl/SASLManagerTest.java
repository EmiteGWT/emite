package com.calclab.emite.core.client.xmpp.sasl;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.Packet;
import com.calclab.emite.core.client.xmpp.session.Credentials;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.xtesting.ConnectionTester;
import com.calclab.suco.testing.events.MockedListener;

public class SASLManagerTest {
    private SASLManager manager;
    private MockedListener<AuthorizationTransaction> listener;
    private ConnectionTester connection;

    @Before
    public void beforeTests() {
	connection = new ConnectionTester();
	manager = new SASLManager(connection, new DecoderRegistry());
	listener = new MockedListener<AuthorizationTransaction>();
	manager.onAuthorized(listener);
    }

    @Test
    public void shouldHandleSuccessWhenAuthorizationSent() {
	manager.sendAuthorizationRequest(createTicket(uri("me@domain"), "password"));
	connection.receives("<success xmlns=\"urn:ietf:params:xml:ns:xmpp-sasl\"/>");
	assertTrue(listener.isCalledOnce());
	assertSame(AuthorizationTransaction.State.succeed, listener.getValue(0).getState());
    }

    @Test
    public void shouldHanonStanzadleFailure() {
	manager.sendAuthorizationRequest(createTicket(uri("node@domain"), "password"));
	connection.receives("<failure xmlns=\"urn:ietf:params:xml:ns:xmpp-sasl\"><not-authorized/></failure>");
	assertTrue(listener.isCalledOnce());
	assertSame(AuthorizationTransaction.State.failed, listener.getValue(0).getState());
    }

    @Test
    public void shouldSendAnonymousIfAnonymousProvided() {
	manager.sendAuthorizationRequest(createTicket(Credentials.ANONYMOUS, null));
	final IPacket packet = new Packet("auth", "urn:ietf:params:xml:ns:xmpp-sasl").With("mechanism", "ANONYMOUS");
	assertTrue(connection.hasSent(packet));
    }

    @Test
    public void shouldSendPlainAuthorizationUnlessAnonymous() {
	manager.sendAuthorizationRequest(createTicket(uri("node@domain/resource"), "password"));
	final IPacket packet = new Packet("auth", "urn:ietf:params:xml:ns:xmpp-sasl").With("mechanism", "PLAIN");
	assertTrue(connection.hasSent(packet));
    }

    @Test
    public void shouldSendPlainAuthorizationWithoutNode() {
	manager.sendAuthorizationRequest(createTicket(uri("domain/resource"), ""));
	final IPacket packet = new Packet("auth", "urn:ietf:params:xml:ns:xmpp-sasl").With("mechanism", "PLAIN");
	assertTrue(connection.hasSent(packet));
    }

    private AuthorizationTransaction createTicket(final XmppURI uri, final String password) {
	final Credentials credentials = new Credentials(uri, password, Credentials.ENCODING_NONE);
	return new AuthorizationTransaction(credentials);
    }
}
