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

package com.calclab.emite.xep.muc;

import static com.calclab.emite.core.XmppURI.uri;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.calclab.emite.core.XmppURI;
import com.calclab.emite.core.events.ChangedEvent.ChangeType;
import com.calclab.emite.core.stanzas.IQ;
import com.calclab.emite.core.stanzas.IQ.Type;
import com.calclab.emite.im.chat.AbstractChatManagerTest;
import com.calclab.emite.im.chat.ChatProperties;
import com.calclab.emite.xep.muc.Occupant;
import com.calclab.emite.xep.muc.RoomChat;
import com.calclab.emite.xep.muc.RoomChatManager;
import com.calclab.emite.xep.muc.RoomChatManagerImpl;
import com.calclab.emite.xep.muc.RoomInvitation;
import com.calclab.emite.xep.muc.Occupant.Affiliation;
import com.calclab.emite.xep.muc.Occupant.Role;
import com.calclab.emite.xtesting.handlers.MessageReceivedTestHandler;
import com.calclab.emite.xtesting.handlers.OccupantChangedTestHandler;
import com.calclab.emite.xtesting.handlers.RoomChatChangedTestHandler;
import com.google.web.bindery.event.shared.SimpleEventBus;

/**
 * Room manager tests using the old event system (legacy)
 * 
 * @author dani
 * 
 */
public class RoomManagerTest extends AbstractChatManagerTest<RoomChatManager, RoomChat> {

	@Test
	public void shouldAcceptInvitations() {
		final RoomChatChangedTestHandler chatCreatedHandler = new RoomChatChangedTestHandler(ChangeType.created);
		manager.addRoomChatChangedHandler(chatCreatedHandler);

		final String reason = "theReason";
		final XmppURI invitor = uri("friend@host/resource");
		final XmppURI roomURI = uri("room@room.service");
		manager.acceptRoomInvitation(new RoomInvitation(invitor, roomURI, reason));
		assertTrue(chatCreatedHandler.isCalledOnce());
		final RoomChat room = chatCreatedHandler.getLastChat();
		assertEquals("room@room.service/self", room.getURI().toString());
	}

	@Test
	public void shouldAcceptRoomPresenceWithAvatar() {
		final RoomChat room = manager.open(uri("room1@domain/nick"));
		session.receives("<presence to='user@domain/resource' from='room1@domain/otherUser2'>" + "<priority>0</priority>"
				+ "<x xmlns='http://jabber.org/protocol/muc#user'>" + "<item jid='otheruserjid@domain/otherresoruce' affiliation='none' "
				+ "role='participant'/></x>" + "<x xmlns='vcard-temp:x:update'><photo>af70fe6519d6a27a910c427c3bc551dcd36073e7</photo></x>" + "</presence>");
		assertEquals(1, room.getOccupantsCount());
		final Occupant occupant = room.getOccupantByOccupantUri(uri("room1@domain/otherUser2"));
		assertNotNull(occupant);
		assertEquals(Affiliation.none, occupant.getAffiliation());
		assertEquals(Role.participant, occupant.getRole());
	}

	@Test
	public void shouldCreateInstantRoomIfNeeded() {
		manager.open(uri("newroomtest1@rooms.localhost/nick"));
		session.receives("<presence from='newroomtest1@rooms.localhost/nick' to='user@localhost/resource' >" + "<priority>5</priority>"
				+ "<x xmlns='http://jabber.org/protocol/muc#user'>" + "<item affiliation='owner' role='moderator' jid='vjrj@localhost/Psi' />"
				+ "<status code='201' />" + "</x>" + "</presence>");
		session.verifyIQSent(new IQ(Type.set));
	}

	@Test
	public void shouldFireChatMessages() {
		final RoomChat chat = manager.open(uri("room@rooms.domain/user"));
		final MessageReceivedTestHandler handler = new MessageReceivedTestHandler();
		chat.addMessageReceivedHandler(handler);
		session.receives("<message from='room@rooms.domain/other' to='user@domain/resource' " + "type='groupchat'><body>the message body</body></message>");
		assertEquals(1, handler.getCalledTimes());
	}

	@Test
	public void shouldGiveSameRoomsWithSameURIS() {
		final RoomChat room1 = manager.open(uri("room@domain/nick"));
		final RoomChat room2 = manager.open(uri("room@domain/nick"));
		assertSame(room1, room2);
	}

	@Test
	public void shouldIgnoreLetterCaseInURIS() {
		final RoomChat room = manager.open(uri("ROOM@domain/nick"));
		final OccupantChangedTestHandler handler = new OccupantChangedTestHandler();
		room.addOccupantChangedHandler(handler);
		session.receives("<presence to='user@domain/resource' xmlns='jabber:client' from='ROom@domain/otherUser'>"
				+ "<x xmlns='http://jabber.org/protocol/muc#user'>" + "<item role='moderator' affiliation='owner' jid='user@domain' /></x></presence>");
		assertTrue(handler.isCalledOnce());
	}

	/**
	 * Test to ensure that when an invitation is received, the resulting chat
	 * created has the same data properties.
	 */
	@Test
	public void shouldPreserveInvitationProperties() {
		final RoomChatChangedTestHandler chatCreatedHandler = new RoomChatChangedTestHandler(ChangeType.created);
		manager.addRoomChatChangedHandler(chatCreatedHandler);

		final String reason = "theReason";
		final XmppURI invitor = uri("friend@host/resource");
		final XmppURI roomURI = uri("room@room.service");
		final ChatProperties properties = new ChatProperties(roomURI);
		final String testDataKey = "TEST_KEY";
		final String testDataValue = "TEST_VALUE";
		properties.setData(testDataKey, testDataValue);
		manager.acceptRoomInvitation(new RoomInvitation(invitor, roomURI, reason, properties));
		final RoomChat room = chatCreatedHandler.getLastChat();
		assertEquals("Chat property not preserved", testDataValue, room.getProperties().getData(testDataKey));
	}

	@Test
	public void shouldUpdateRoomPresence() {
		final RoomChat room = manager.open(uri("room1@domain/nick"));

		session.receives("<presence to='user@domain/resource' xmlns='jabber:client' from='room1@domain/otherUser'>"
				+ "<x xmlns='http://jabber.org/protocol/muc#user'>" + "<item role='moderator' affiliation='owner' jid='otherUser@domain' /></x></presence>");
		assertEquals(1, room.getOccupantsCount());
		Occupant user = room.getOccupantByOccupantUri(uri("room1@domain/otherUser"));
		assertNotNull(user);
		assertEquals(Affiliation.owner, user.getAffiliation());
		assertEquals(Role.moderator, user.getRole());

		session.receives("<presence to='user@domain/resource' xmlns='jabber:client' from='room1@domain/otherUser'>"
				+ "<x xmlns='http://jabber.org/protocol/muc#user'>" + "<item role='participant' affiliation='member' /></x></presence>");
		assertEquals(1, room.getOccupantsCount());
		user = room.getOccupantByOccupantUri(uri("room1@domain/otherUser"));
		assertNotNull(user);
		assertEquals(Affiliation.member, user.getAffiliation());
		assertEquals(Role.participant, user.getRole());

		session.receives("<presence to='user@domain/res1' type='unavailable' " + "xmlns='jabber:client' from='room1@domain/otherUser'>"
				+ "<status>custom message</status><x xmlns='http://jabber.org/protocol/muc#user'>" + "<item role='none' affiliation='member' /></x></presence>");
		assertEquals(0, room.getOccupantsCount());

	}
	
	@Test
	public void shouldEventWhenAChatIsClosed() {
		final RoomChat chat = manager.open(uri("other@domain/resource"));
		final RoomChatChangedTestHandler handler = new RoomChatChangedTestHandler(ChangeType.closed);
		manager.addRoomChatChangedHandler(handler);
		manager.close(chat);
		assertTrue(handler.isCalledOnce());
	}

	@Test
	public void shouldEventWhenChatCreated() {
		final RoomChatChangedTestHandler handler = new RoomChatChangedTestHandler(ChangeType.created);
		manager.addRoomChatChangedHandler(handler);
		manager.open(OTHER);
		assertTrue(handler.isCalledOnce());
	}

	@Override
	protected RoomChatManager createChatManager() {
		return new RoomChatManagerImpl(new SimpleEventBus(), session, new RoomChatSelectionStrategy());
	}
}
