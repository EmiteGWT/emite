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

package com.calclab.emite.xtesting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import com.calclab.emite.base.xml.HasXML;
import com.calclab.emite.base.xml.XMLBuilder;
import com.calclab.emite.base.xml.XMLPacket;
import com.calclab.emite.core.IQCallback;
import com.calclab.emite.core.XmppURI;
import com.calclab.emite.core.conn.XmppConnection;
import com.calclab.emite.core.conn.bosh.StreamSettings;
import com.calclab.emite.core.events.IQReceivedEvent;
import com.calclab.emite.core.events.MessageReceivedEvent;
import com.calclab.emite.core.events.PresenceReceivedEvent;
import com.calclab.emite.core.session.Credentials;
import com.calclab.emite.core.session.SessionStatus;
import com.calclab.emite.core.session.XmppSessionImpl;
import com.calclab.emite.core.session.sasl.SASLManagerImpl;
import com.calclab.emite.core.stanzas.IQ;
import com.calclab.emite.core.stanzas.Message;
import com.calclab.emite.core.stanzas.Presence;
import com.calclab.emite.core.stanzas.Stanza;
import com.calclab.emite.core.stanzas.IQ.Type;
import com.calclab.emite.xtesting.matchers.EmiteAsserts;
import com.calclab.emite.xtesting.matchers.IsPacketLike;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;

public class XmppSessionTester extends XmppSessionImpl {

	private EventBus eventBus = new SimpleEventBus();
	private XmppConnection connection = new XmppConnectionTester();
	
	private XmppURI currentUser;
	private final List<Stanza> sent;
	private IQ lastIQSent;
	private IQCallback lastIQResponseHandler;

	public XmppSessionTester() {
		this((XmppURI) null);
	}

	/**
	 * Create a new SessionTester and login if user provided
	 * 
	 * @param user
	 *            optional user to login
	 */
	public XmppSessionTester(final String user) {
		this(XmppURI.uri(user));
	}

	/**
	 * Create a new SessionTester and login if user provided
	 * 
	 * @param user
	 *            optional user to login
	 */
	public XmppSessionTester(final XmppURI user) {
		super(eventBus, connection, new SASLManagerImpl(eventBus, connection));
		sent = new ArrayList<Stanza>();
		if (user != null) {
			setLoggedIn(user);
		}
	}

	public void answerSuccess(final IQ iq) {
		lastIQResponseHandler.onIQSuccess(iq);
	}

	public void answerFailure(final IQ iq) {
		lastIQResponseHandler.onIQFailure(iq);
	}

	@Override
	public XmppURI getCurrentUserURI() {
		return currentUser;
	}

	@Override
	public boolean isReady() {
		return currentUser != null;
	}

	@Deprecated
	public void login(final XmppURI user, final String password) {
		login(new Credentials(user, password));
	}
	
	@Override
	public void login(final Credentials credentials) {
		setLoggedIn(credentials.getUri());
	}

	@Override
	public void logout() {
		if (currentUser != null) {
			setStatus(SessionStatus.loggingOut);
			currentUser = null;
			setStatus(SessionStatus.disconnected);
		}
	}

	@Override
	public StreamSettings pause() {
		return null;
	}

	public void receives(final Message message) {
		eventBus.fireEvent(new MessageReceivedEvent(message));
	}

	public void receives(final Presence presence) {
		eventBus.fireEvent(new PresenceReceivedEvent(presence));
	}

	public void receives(final String received) {
		final XMLPacket stanza = XMLBuilder.fromXML(received);
		final String name = stanza.getTagName();
		if (name.equals("message")) {
			eventBus.fireEvent(new MessageReceivedEvent(new Message(stanza)));
		} else if (name.equals("presence")) {
			eventBus.fireEvent(new PresenceReceivedEvent(new Presence(stanza)));
		} else if (name.equals("iq")) {
			eventBus.fireEvent(new IQReceivedEvent(new IQ(stanza)));
		} else
			throw new RuntimeException("WHAT IS THIS? (" + name + "): " + stanza.toString());

	}

	@Override
	public void resume(final XmppURI userURI, final StreamSettings settings) {
	}

	@Override
	public void send(final Stanza packet) {
		sent.add(packet);
	}

	@Override
	public void sendIQ(final String category, final IQ iq, final IQCallback iqHandler) {
		lastIQSent = iq;
		lastIQResponseHandler = iqHandler;
	}

	public void setCurrentUser(final XmppURI currentUser) {
		this.currentUser = currentUser;
	}

	public void setLoggedIn(final String uri) {
		setLoggedIn(XmppURI.uri(uri));
	}

	public void setLoggedIn(final XmppURI userURI) {
		currentUser = userURI;
		setStatus(SessionStatus.loggedIn);
	}

	public void setReady() {
		setStatus(SessionStatus.ready);
	}

	public IQCallback verifyIQSent(final IQ iq) {
		assertNotNull(lastIQSent);
		EmiteAsserts.assertPacketLike(iq, lastIQSent);
		return lastIQResponseHandler;
	}

	public void verifyIQSent(final String xml) {
		verifyIQSent(new IQ(XMLBuilder.fromXML(xml)));
	}

	public void verifyNotSent(final XMLPacket packet) {
		assertNotContains(packet, sent);
	}

	public void verifyNotSent(final String xml) {
		verifyNotSent(XMLBuilder.fromXML(xml));
	}

	public void verifySent(final XMLPacket packet) {
		assertContains(packet, sent);
	}

	public void verifySent(final String expected) {
		verifySent(XMLBuilder.fromXML(expected));
	}

	public void verifySentNothing() {
		assertEquals("number of sent stanzas", 0, sent.size());
	}

	private void assertContains(final HasXML expected, final List<? extends HasXML> list) {
		final StringBuilder buffer = new StringBuilder();
		final boolean isContained = contains(expected, list, buffer);
		assertTrue("Expected " + expected + " contained in " + buffer, isContained);
	}

	private void assertNotContains(final HasXML expected, final List<? extends HasXML> list) {
		final StringBuilder buffer = new StringBuilder();
		final boolean isContained = contains(expected, list, buffer);
		assertFalse("Expected " + expected + " contained in\n" + buffer, isContained);
	}

	private boolean contains(final HasXML expected, final List<? extends HasXML> list, final StringBuilder buffer) {
		boolean isContained = false;
		final IsPacketLike matcher = new IsPacketLike(expected);
		for (final HasXML packet : list) {
			buffer.append("[").append(packet.toString()).append("]");
			isContained = isContained ? isContained : matcher.matches(packet, System.out);
		}
		return isContained;
	}

}
