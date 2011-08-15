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

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.client.events.MessageReceivedEvent;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.Presence.Show;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.AbstractChat;
import com.calclab.emite.im.client.chat.AbstractChatTest;
import com.calclab.emite.im.client.chat.ChatProperties;
import com.calclab.emite.xep.muc.client.subject.RoomSubject;
import com.calclab.emite.xtesting.handlers.MessageTestHandler;
import com.calclab.emite.xtesting.handlers.OccupantChangedTestHandler;
import com.calclab.emite.xtesting.handlers.RoomSubjectChangedTestHandler;
import com.calclab.emite.xtesting.handlers.StateChangedTestHandler;

public class RoomTest extends AbstractChatTest {
	private RoomChat room;
	private XmppURI userURI;
	private XmppURI roomURI;

	@Before
	public void beforeTests() {
		userURI = uri("user@domain/res");
		roomURI = uri("room@domain/nick");
		session.setLoggedIn(userURI);
		final RoomChatManager manager = new RoomChatManager(session);
		final ChatProperties properties = new ChatProperties(roomURI, userURI, null);
		room = (RoomChat) manager.openChat(properties, true);
	}

	@Override
	public AbstractChat getChat() {
		return room;
	}

	@Test
	public void shouldAddOccupantAndFireListeners() {
		final OccupantChangedTestHandler handler = new OccupantChangedTestHandler();
		room.addOccupantChangedHandler(handler);
		final XmppURI occupantUri = uri("room@domain/user");
		final Occupant occupant = room.setOccupantPresence(userURI, occupantUri, "aff", "role", Show.unknown, null);
		assertTrue(handler.isCalledOnce());
		final Occupant result = room.getOccupantByOccupantUri(occupantUri);
		assertEquals(occupant, result);
	}

	@Test
	public void shouldExitAndLockTheRoomWhenLoggedOut() {
		receiveInstantRoomCreation(userURI, roomURI);
		session.logout();
		assertEquals("locked", room.getChatState());
		session.verifySent("<presence to='room@domain/nick' type='unavailable'/>");
	}

	@Test
	public void shouldFireListenersWhenMessage() {
		final MessageTestHandler handler = new MessageTestHandler();
		room.addMessageReceivedHandler(handler);
		final Message message = new Message("message", uri("room@domain"), uri("someone@domain/res"));
		room.getChatEventBus().fireEvent(new MessageReceivedEvent(message));
		assertEquals(message, handler.getLastMessage());
	}

	@Test
	public void shouldFireListenersWhenSubjectChange() {
		final RoomSubjectChangedTestHandler handler = new RoomSubjectChangedTestHandler();
		RoomSubject.addRoomSubjectChangedHandler(room, handler);

		final XmppURI occupantURI = uri("someone@domain/res");
		room.getChatEventBus().fireEvent(new MessageReceivedEvent(new Message(null, uri("room@domain"), occupantURI).Subject("the subject")));
		assertEquals(1, handler.getCalledTimes());

		assertEquals(occupantURI, handler.getLastEvent().getOccupantUri());
		assertEquals("the subject", handler.getLastEvent().getSubject());
	}

	@Test
	public void shouldRemoveOccupant() {
		final OccupantChangedTestHandler handler = new OccupantChangedTestHandler("removed");
		room.addOccupantChangedHandler(handler);
		final XmppURI occupantUri = uri("room@domain/name");
		room.setOccupantPresence(userURI, occupantUri, "owner", "participant", Show.notSpecified, null);
		assertEquals(1, room.getOccupantsCount());
		room.removeOccupant(occupantUri);
		assertEquals(0, room.getOccupantsCount());
		assertEquals(1, handler.getCalledTimes());
		assertNull(room.getOccupantByOccupantUri(occupantUri));
	}

	@Test
	public void shouldSendRoomInvitation() {
		room.sendInvitationTo(uri("otherUser@domain/resource"), "this is the reason");
		session.verifySent("<message from='" + userURI + "' to='" + roomURI.getJID() + "'><x xmlns='http://jabber.org/protocol/muc#user'>"
				+ "<invite to='otheruser@domain/resource'><reason>this is the reason</reason></invite></x></message>");
	}

	@Test
	public void shouldSendRoomPresenceWhenCreated() {
		session.verifySent("<presence to='room@domain/nick'><x xmlns='http://jabber.org/protocol/muc' /></presence>");
	}

	@Test
	public void shouldUnlockWhenInstantRoomIsCreated() {
		final StateChangedTestHandler handler = new StateChangedTestHandler();
		room.addChatStateChangedHandler(false, handler);
		assertEquals("locked", room.getChatState());
		receiveInstantRoomCreation(userURI, roomURI);
		assertTrue(handler.isCalledOnce());
	}

	@Test
	public void shouldUpdateOccupantAndFireListeners() {
		final OccupantChangedTestHandler handler = new OccupantChangedTestHandler("modified");
		room.addOccupantChangedHandler(handler);
		final XmppURI occupantUri = uri("room@domain/name");
		final Occupant occupant = room.setOccupantPresence(userURI, occupantUri, "owner", "participant", Show.notSpecified, null);
		final Occupant occupant2 = room.setOccupantPresence(userURI, occupantUri, "admin", "moderator", Show.notSpecified, null);
		assertEquals(1, handler.getCalledTimes());
		assertSame(occupant, occupant2);
	}

	private void receiveInstantRoomCreation(final XmppURI userUri, final XmppURI room) {
		session.receives("<presence to='user@domain/res' from='" + room + "'>" + "<x xmlns='http://jabber.org/protocol/muc#user'>"
				+ "<item affiliation='owner' role='moderator' jid='" + userUri + "' /><status code='201'/></x></presence>");
		session.verifyIQSent("<iq to='" + room.getJID() + "' type='set'>" + "<query xmlns='http://jabber.org/protocol/muc#owner'>"
				+ "<x xmlns='jabber:x:data' type='submit'/></query></iq>");
		session.answerSuccess();
	}

}
