package com.calclab.emite.core.client.xmpp.session;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;
import static com.calclab.suco.testing.events.Eventito.anyListener;
import static com.calclab.suco.testing.events.Eventito.fire;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.client.bosh.ConnectionTestHelper;
import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.Packet;
import com.calclab.emite.core.client.xmpp.resource.ResourceBindingManager;
import com.calclab.emite.core.client.xmpp.sasl.AuthorizationTransaction;
import com.calclab.emite.core.client.xmpp.sasl.SASLManager;
import com.calclab.emite.core.client.xmpp.session.Session.State;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.Presence;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.suco.client.events.Listener;
import com.calclab.suco.testing.events.MockedListener;

public class SessionTest {

    private SessionImpl session;
    private SASLManager saslManager;
    private ResourceBindingManager bindingManager;
    private ConnectionTestHelper helper;
    private IMSessionManager iMSessionManager;

    @Before
    public void beforeTest() {
	helper = new ConnectionTestHelper();

	saslManager = mock(SASLManager.class);
	bindingManager = mock(ResourceBindingManager.class);
	iMSessionManager = mock(IMSessionManager.class);
	session = new SessionImpl(helper.connection, saslManager, bindingManager, iMSessionManager);

    }

    @Test
    public void shouldConnectOnLogin() {
	session.login(uri("name@domain/resource"), "password");
	verify(helper.connection).connect();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldEventMessages() {
	final MockedListener<Message> listener = new MockedListener<Message>();
	session.onMessage(listener);

	fire(new Packet("message")).when(helper.connection).onStanzaReceived(anyListener());
	assertTrue(listener.isCalledOnce());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldEventPresences() {
	final MockedListener<Presence> listener = new MockedListener<Presence>();
	session.onPresence(listener);

	fire(new Packet("presence")).when(helper.connection).onStanzaReceived(anyListener());
	assertTrue(listener.isCalledOnce());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldEventStateChanges() {
	final Listener<Session.State> listener = mock(Listener.class);
	session.onStateChanged(listener);
	session.setState(Session.State.ready);
	verify(listener).onEvent(same(Session.State.ready));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldHandleFailedAuthorizationResult() {
	fire(new AuthorizationTransaction(uri("node@domain"), "password", AuthorizationTransaction.State.failed)).when(
		saslManager).onAuthorized(anyListener());
	verify(helper.connection).disconnect();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldHandleSucceedAuthorizationResult() {
	fire(
		new AuthorizationTransaction(uri("node@domain/resource"), "password",
			AuthorizationTransaction.State.succeed)).when(saslManager).onAuthorized(anyListener());

	assertEquals(Session.State.authorized, session.getState());
	verify(helper.connection).restartStream();
	verify(bindingManager).bindResource(anyString());
    }

    @Test
    public void shouldLoginWhenSessionCreated() {

	final MockedListener<State> onStateChanged = new MockedListener<State>();
	session.onStateChanged(onStateChanged);

	createSession(uri("name@domain/resource"));
	assertTrue(onStateChanged.isCalledWithEquals(State.loggedIn));
    }

    @Test
    public void shouldQueueOutcomingStanzas() {
	session.send(new Message("the Message", uri("other@domain")));
	verify(helper.connection, never()).send((IPacket) anyObject());
	createSession(uri("name@domain/resource"));
	session.setReady();
	verify(helper.connection).send((IPacket) anyObject());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldRequestSessionWhenBinded() {
	final XmppURI uri = uri("name@domain/resource");
	fire(uri).when(bindingManager).onBinded(anyListener());
	verify(iMSessionManager).requestSession(same(uri));
    }

    @Test
    public void shouldStopAndDisconnectWhenLoggedOut() {
    }

    @SuppressWarnings("unchecked")
    private void createSession(final XmppURI uri) {
	fire(uri).when(iMSessionManager).onSessionCreated(anyListener());
    }
}
