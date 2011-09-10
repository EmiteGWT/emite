/*
 * ((e)) emite: A pure Google Web Toolkit XMPP library
 * Copyright (c) 2008-2011 The Emite development team
 * 
 * This file is part of Emite.
 *
 * Emite is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * Emite is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with Emite.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.calclab.emite.core.client.xmpp.session;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.client.packet.Packet;
import com.calclab.emite.core.client.xmpp.resource.ResourceBindResultEvent;
import com.calclab.emite.core.client.xmpp.resource.ResourceBindingManager;
import com.calclab.emite.core.client.xmpp.sasl.AuthorizationResultEvent;
import com.calclab.emite.core.client.xmpp.sasl.SASLManager;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.xtesting.XmppConnectionTester;
import com.calclab.emite.xtesting.handlers.BeforeStanzaSentTestHandler;
import com.calclab.emite.xtesting.handlers.MessageReceivedTestHandler;
import com.calclab.emite.xtesting.handlers.PresenceReceivedTestHandler;
import com.calclab.emite.xtesting.handlers.SessionStateChangedTestHandler;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;

public class XmppSessionTests {
	private XmppSessionImpl session;
	private SASLManager saslManager;
	private ResourceBindingManager bindingManager;
	private IMSessionManager iMSessionManager;
	private XmppConnectionTester connection;
	private EventBus eventBus;

	@Before
	public void beforeTest() {
		connection = new XmppConnectionTester();
		eventBus = new SimpleEventBus();
		saslManager = mock(SASLManager.class);
		bindingManager = mock(ResourceBindingManager.class);
		iMSessionManager = mock(IMSessionManager.class);
		session = new XmppSessionImpl(eventBus, connection, saslManager, bindingManager, iMSessionManager);
	}

	@Test
	public void shouldConnectOnLogin() {
		assertFalse(connection.isConnected());
		session.login(uri("name@domain/resource"), "password");
		assertTrue(connection.isConnected());
	}

	/**
	 * Issue 324
	 */
	@Test
	public void shouldEventBeforeSendStanzaEvents() {
		// we need to log in before
		eventBus.fireEvent(new SessionRequestResultEvent(uri("user@domain")));
		final BeforeStanzaSentTestHandler handler = new BeforeStanzaSentTestHandler();
		session.addBeforeStanzaSentHandler(handler);
		final Packet packet = new Packet("message");
		session.send(packet);
		assertTrue(handler.isCalledOnce());
		assertSame(packet, handler.getLastEvent().getStanza());
	}

	@Test
	public void shouldEventMessages() {
		final MessageReceivedTestHandler handler = new MessageReceivedTestHandler();
		session.addMessageReceivedHandler(handler);
		final Message message = new Message("message");
		connection.receives(message);
		assertTrue(handler.isCalledOnce());
	}

	@Test
	public void shouldEventPresences() {
		final PresenceReceivedTestHandler handler = new PresenceReceivedTestHandler();
		session.addPresenceReceivedHandler(handler);
		connection.receives(new Packet("presence"));
		assertTrue(handler.isCalledOnce());
	}

	@Test
	public void shouldEventStateChanges() {
		final SessionStateChangedTestHandler handler = new SessionStateChangedTestHandler();
		session.addSessionStateChangedHandler(false, handler);
		session.setSessionState(SessionState.ready);
		assertSame(SessionState.ready, handler.getLastSessionState());
	}

	@Test
	public void shouldHandleFailedAuthorizationResult() {
		connection.connect();
		eventBus.fireEvent(new AuthorizationResultEvent());
		assertFalse(connection.isConnected());
	}

	@Test
	public void shouldHandleSucceedAuthorizationResult() {
		eventBus.fireEvent(new AuthorizationResultEvent(new Credentials(uri("node@domain"), "pass")));

		assertEquals(SessionState.authorized, session.getSessionState());
		assertTrue(connection.isStreamRestarted());
		verify(bindingManager).bindResource(anyString());
	}

	@Test
	public void shouldLoginWhenSessionCreated() {
		final SessionStateChangedTestHandler handler = new SessionStateChangedTestHandler();
		session.addSessionStateChangedHandler(false, handler);
		eventBus.fireEvent(new SessionRequestResultEvent(uri("me@domain")));
		assertSame(SessionState.loggedIn, handler.getLastSessionState());
	}

	@Test
	public void shouldQueueOutcomingStanzas() {
		assertEquals(0, connection.getSentSize());
		session.send(new Message("the Message", uri("other@domain")));
		assertEquals(0, connection.getSentSize());
		eventBus.fireEvent(new SessionRequestResultEvent(uri("name@domain/resource")));
		session.setSessionState(SessionState.ready);
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
