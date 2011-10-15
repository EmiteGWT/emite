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

package com.calclab.emite.core.client.session;

import static com.calclab.emite.core.client.uri.XmppURI.uri;
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

import com.calclab.emite.core.client.events.AuthorizationResultEvent;
import com.calclab.emite.core.client.session.Credentials;
import com.calclab.emite.core.client.session.SessionStatus;
import com.calclab.emite.core.client.session.XmppSessionImpl;
import com.calclab.emite.core.client.session.sasl.SASLManager;
import com.calclab.emite.core.client.stanzas.Message;
import com.calclab.emite.core.client.uri.XmppURI;
import com.calclab.emite.core.client.xml.XMLBuilder;
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
	private XmppConnectionTester connection;
	private EventBus eventBus;

	@Before
	public void beforeTest() {
		connection = new XmppConnectionTester();
		eventBus = new SimpleEventBus();
		saslManager = mock(SASLManager.class);
		session = new XmppSessionImpl(eventBus, connection, saslManager);
	}

	@Test
	public void shouldConnectOnLogin() {
		assertFalse(connection.isConnected());
		session.login(new Credentials(uri("name@domain/resource"), "password"));
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
		final Message packet = new Message();
		session.send(packet);
		assertTrue(handler.isCalledOnce());
		assertSame(packet, handler.getLastEvent().getStanza());
	}

	@Test
	public void shouldEventMessages() {
		final MessageReceivedTestHandler handler = new MessageReceivedTestHandler();
		session.addMessageReceivedHandler(handler);
		final Message message = new Message("message");
		connection.receives(message.getXML());
		assertTrue(handler.isCalledOnce());
	}

	@Test
	public void shouldEventPresences() {
		final PresenceReceivedTestHandler handler = new PresenceReceivedTestHandler();
		session.addPresenceReceivedHandler(handler);
		connection.receives(XMLBuilder.create("presence").getXML());
		assertTrue(handler.isCalledOnce());
	}

	@Test
	public void shouldEventStateChanges() {
		final SessionStateChangedTestHandler handler = new SessionStateChangedTestHandler();
		session.addSessionStatusChangedHandler(false, handler);
		session.setStatus(SessionStatus.ready);
		assertSame(SessionStatus.ready, handler.getLastSessionStatus());
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

		assertEquals(SessionStatus.authorized, session.getStatus());
		assertTrue(connection.isStreamRestarted());
		verify(bindingManager).bindResource(anyString());
	}

	@Test
	public void shouldLoginWhenSessionCreated() {
		final SessionStateChangedTestHandler handler = new SessionStateChangedTestHandler();
		session.addSessionStatusChangedHandler(false, handler);
		eventBus.fireEvent(new SessionRequestResultEvent(uri("me@domain")));
		assertSame(SessionStatus.loggedIn, handler.getLastSessionStatus());
	}

	@Test
	public void shouldQueueOutcomingStanzas() {
		assertEquals(0, connection.getSentSize());
		session.send(new Message("the Message", uri("other@domain")));
		assertEquals(0, connection.getSentSize());
		eventBus.fireEvent(new SessionRequestResultEvent(uri("name@domain/resource")));
		session.setStatus(SessionStatus.ready);
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
		//TODO
	}

}
