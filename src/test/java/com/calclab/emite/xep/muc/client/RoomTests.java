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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.ChatStates;
import com.calclab.emite.xtesting.XmppSessionTester;
import com.calclab.emite.xtesting.handlers.MessageTestHandler;

public class RoomTests {

	private XmppSessionTester session;
	private RoomChatManager manager;
	private Room room;
	private XmppURI userURI;
	private XmppURI roomURI;

	@Before
	public void beforeTest() {
		userURI = uri("user@domain/res");
		roomURI = uri("room@domain/user");
		session = new XmppSessionTester(userURI);
		manager = new RoomChatManager(session);
		room = (Room) manager.open(roomURI);
	}

	@Test
	public void shouldIdentifyUserAndAdminMessages() {
		final Message adminMessage = new Message("admin");
		adminMessage.setFrom(roomURI.getJID());
		assertFalse(room.isUserMessage(adminMessage));
		final Message userMessage = new Message("user");
		userMessage.setFrom(XmppURI.uri("room@domain/someone"));
		assertTrue(room.isUserMessage(userMessage));
	}

	@Test
	public void shouldInterceptOutcomingMessages() {
		// a little hack
		room.getProperties().setState(ChatStates.ready);
		final MessageTestHandler handler = new MessageTestHandler();
		room.addBeforeSendMessageHandler(handler);
		room.send(new Message("body"));
		assertTrue(handler.isCalledOnce());
		assertEquals("body", handler.getLastMessage().getBody());
	}
}
