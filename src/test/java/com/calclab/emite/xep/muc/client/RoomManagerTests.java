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

package com.calclab.emite.xep.muc.client;

import static com.calclab.emite.core.client.uri.XmppURI.uri;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.client.events.ChangedEvent.ChangeType;
import com.calclab.emite.core.client.stanzas.IQ;
import com.calclab.emite.core.client.uri.XmppURI;
import com.calclab.emite.im.client.chat.ChatProperties;
import com.calclab.emite.xtesting.XmppSessionTester;
import com.calclab.emite.xtesting.handlers.RoomChatChangedTestHandler;
import com.google.web.bindery.event.shared.SimpleEventBus;

/**
 * RoomChatManager tests using the new event system
 * 
 * @author dani
 * 
 */
public class RoomManagerTests {

	private static final XmppURI USER = XmppURI.uri("user@domain/res");
	private XmppSessionTester session;
	private RoomChatManager manager;

	@Before
	public void beforeTests() {
		session = new XmppSessionTester(USER);
		manager = new RoomChatManagerImpl(new SimpleEventBus(), session, new RoomChatSelectionStrategy());
	}

	@Test
	public void shouldAcceptInvitations() {
		final RoomChatChangedTestHandler handler = new RoomChatChangedTestHandler();
		final ChatProperties properties = new ChatProperties(USER);
		manager.addRoomChatChangedHandler(handler);
		final RoomInvitation invitation = new RoomInvitation(uri("friend@host/resource"), uri("room@room.service"), "theReason", properties);
		manager.acceptRoomInvitation(invitation);
		assertEquals(2, handler.getCalledTimes());
		assertEquals(ChangeType.created, handler.getEvent(0).getChangeType());
		assertEquals(ChangeType.opened, handler.getEvent(1).getChangeType());
	}

	@Test
	public void shouldCreateInstantRoomIfNeeded() {
		manager.open(uri("newroomtest1@rooms.localhost/nick"));
		session.receives("<presence from='newroomtest1@rooms.localhost/nick' to='user@localhost/resource' >" + "<priority>5</priority>"
				+ "<x xmlns='http://jabber.org/protocol/muc#user'>" + "<item affiliation='owner' role='moderator' jid='vjrj@localhost/Psi' />"
				+ "<status code='201' />" + "</x>" + "</presence>");
		session.verifyIQSent(new IQ(IQ.Type.set));
	}
}
