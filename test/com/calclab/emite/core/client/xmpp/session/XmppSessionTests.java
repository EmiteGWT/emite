package com.calclab.emite.core.client.xmpp.session;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.core.client.events.MessageEvent;
import com.calclab.emite.core.client.events.MessageHandler;
import com.calclab.emite.core.client.events.PresenceEvent;
import com.calclab.emite.core.client.events.PresenceHandler;
import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.Packet;
import com.calclab.emite.core.client.xmpp.resource.ResourceBindResultEvent;
import com.calclab.emite.core.client.xmpp.resource.ResourceBindingManager;
import com.calclab.emite.core.client.xmpp.sasl.AuthorizationResultEvent;
import com.calclab.emite.core.client.xmpp.sasl.SASLManager;
import com.calclab.emite.core.client.xmpp.session.XmppSession.SessionStates;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.xtesting.XmppConnectionTester;
import com.calclab.emite.xtesting.handlers.StateChangedTestHandler;

public class XmppSessionTests {
    private XmppSessionLogic session;
    private SASLManager saslManager;
    private ResourceBindingManager bindingManager;
    private IMSessionManager iMSessionManager;
    private XmppConnectionTester connection;
    private IPacket incomingPacket;
    private EmiteEventBus eventBus;

    @Before
    public void beforeTest() {
	connection = new XmppConnectionTester();
	eventBus = connection.getEventBus();
	saslManager = mock(SASLManager.class);
	bindingManager = mock(ResourceBindingManager.class);
	iMSessionManager = mock(IMSessionManager.class);
	session = new XmppSessionLogic(connection, saslManager, bindingManager, iMSessionManager);

    }

    @Test
    public void shouldConnectOnLogin() {
	assertFalse(connection.isConnected());
	session.login(uri("name@domain/resource"), "password");
	assertTrue(connection.isConnected());
    }

    @Test
    public void shouldEventMessages() {
	incomingPacket = null;
	session.addMessageReceivedHandler(new MessageHandler() {
	    @Override
	    public void onMessage(final MessageEvent event) {
		incomingPacket = event.getMessage();
	    }
	});
	connection.receives(new Packet("message"));
	assertNotNull(incomingPacket);
    }

    @Test
    public void shouldEventPresences() {
	incomingPacket = null;
	session.addPresenceReceivedHandler(new PresenceHandler() {
	    @Override
	    public void onPresence(final PresenceEvent event) {
		incomingPacket = event.getPresence();
	    }
	});
	connection.receives(new Packet("presence"));
	assertNotNull(incomingPacket);
    }

    @Test
    public void shouldEventStateChanges() {
	final StateChangedTestHandler handler = new StateChangedTestHandler();
	session.addSessionStateChangedHandler(false, handler);
	session.setSessionState(SessionStates.ready);
	assertSame(SessionStates.ready, handler.getLastState());
    }

    @Test
    public void shouldHandleFailedAuthorizationResult() {
	connection.connect();
	eventBus.fireEvent(new AuthorizationResultEvent());
	assertFalse(connection.isConnected());
    }

    @Test
    public void shouldHandleSucceedAuthorizationResult() {
	eventBus.fireEvent(new AuthorizationResultEvent(new Credentials(uri("node@domain"), "pass",
		Credentials.ENCODING_NONE)));

	assertEquals(SessionStates.authorized, session.getSessionState());
	assertTrue(connection.isStreamRestarted());
	verify(bindingManager).bindResource(anyString());
    }

    @Test
    public void shouldLoginWhenSessionCreated() {
	final StateChangedTestHandler handler = new StateChangedTestHandler();
	session.addSessionStateChangedHandler(false, handler);
	eventBus.fireEvent(new SessionRequestResultEvent(uri("me@domain")));
	assertSame(SessionStates.loggedIn, handler.getLastState());
    }

    @Test
    public void shouldQueueOutcomingStanzas() {
	assertEquals(0, connection.getSentSize());
	session.send(new Message("the Message", uri("other@domain")));
	assertEquals(0, connection.getSentSize());
	eventBus.fireEvent(new SessionRequestResultEvent(uri("name@domain/resource")));
	session.setReady();
	assertEquals(1, connection.getSentSize());
    }

    @Test
    public void shouldRequestSessionWhenBinded() {
	final XmppURI uri = uri("name@domain/resource");
	eventBus.fireEvent(new ResourceBindResultEvent(uri));
	verify(iMSessionManager).requestSession(same(uri));
    }

    @Test
    public void shouldStopAndDisconnectWhenLoggedOut() {
    }

}
