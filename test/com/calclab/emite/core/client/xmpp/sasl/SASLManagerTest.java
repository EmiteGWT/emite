package com.calclab.emite.core.client.xmpp.sasl;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.client.bosh.ConnectionTestHelper;
import com.calclab.emite.core.client.packet.Packet;
import com.calclab.suco.testing.events.MockedListener;

public class SASLManagerTest {
    private SASLManager manager;
    private MockedListener<AuthorizationTransaction> listener;
    private ConnectionTestHelper helper;

    @Before
    public void beforeTests() {
	helper = new ConnectionTestHelper();
	manager = new SASLManager(helper.getConnection());
	listener = new MockedListener<AuthorizationTransaction>();
	manager.onAuthorized(listener);
    }

    @Test
    public void shouldHandleSuccessWhenAuthorizationSent() {
	manager.sendAuthorizationRequest(new AuthorizationTransaction(uri("me@domain"), "password"));
	helper.simulateReception("<success xmlns=\"urn:ietf:params:xml:ns:xmpp-sasl\"/>");
	assertTrue(listener.isCalledOnce());
	assertSame(AuthorizationTransaction.State.succeed, listener.getValue(0).getState());
    }

    @Test
    public void shouldHanonStanzadleFailure() {
	manager.sendAuthorizationRequest(new AuthorizationTransaction(uri("node@domain"), "password"));
	helper.simulateReception("<failure xmlns=\"urn:ietf:params:xml:ns:xmpp-sasl\"><not-authorized/></failure>");
	assertTrue(listener.isCalledOnce());
	assertSame(AuthorizationTransaction.State.failed, listener.getValue(0).getState());
    }

    @Test
    public void shouldSendAnonymousIfAnonymousProvided() {
	manager.sendAuthorizationRequest(new AuthorizationTransaction(SASLManager.ANONYMOUS, null));
	helper.verifySentLike(new Packet("auth", "urn:ietf:params:xml:ns:xmpp-sasl").With("mechanism", "ANONYMOUS"));
    }

    @Test
    public void shouldSendPlainAuthorizationUnlessAnonymous() {
	manager.sendAuthorizationRequest(new AuthorizationTransaction(uri("node@domain/resource"), "password"));
	helper.verifySentLike(new Packet("auth", "urn:ietf:params:xml:ns:xmpp-sasl").With("mechanism", "PLAIN"));
    }

    @Test
    public void shouldSendPlainAuthorizationWithoutNode() {
	manager.sendAuthorizationRequest(new AuthorizationTransaction(uri("domain/resource"), null));
	helper.verifySentLike(new Packet("auth", "urn:ietf:params:xml:ns:xmpp-sasl").With("mechanism", "PLAIN"));
    }

}
