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

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.client.events.MessageReceivedEvent;
import com.calclab.emite.core.client.stanzas.Message;
import com.calclab.emite.core.client.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.ChatStatus;
import com.calclab.emite.xep.muc.client.RoomChat;
import com.calclab.emite.xep.muc.client.RoomChatManagerImpl;
import com.calclab.emite.xtesting.XmppSessionTester;
import com.calclab.emite.xtesting.handlers.RoomSubjectChangedTestHandler;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;

/**
 * Test RoomSubject class
 * 
 */
public class RoomSubjectTests {

	private EventBus eventBus;
	private RoomChat room;
	private XmppURI roomUri;
	private XmppSessionTester session;
	private XmppURI userUri;
	private XmppURI occupantUri;

	@Before
	public void beforeTests() {
		eventBus = new SimpleEventBus();
		userUri = XmppURI.uri("user@domain/resource");
		roomUri = XmppURI.uri("room@conference.domain");
		occupantUri = XmppURI.uri("room@conference.domain/user");
		session = new XmppSessionTester(userUri);
		final RoomChatManagerImpl manager = new RoomChatManagerImpl(eventBus, session, new RoomChatSelectionStrategy());
		room = manager.open(roomUri);
		room.getProperties().setStatus(ChatStatus.ready);
	}

	@Test
	public void shouldChangeSubject() {
		room.requestSubjectChange("Some subject");
		session.verifySent("<message type='groupchat' from='" + userUri + "' to='" + room.getURI().getJID()
				+ "' ><subject>Some subject</subject></message></body>");
	}

	@Test
	public void shouldHandleRoomSubjectChangeEvents() {
		final RoomSubjectChangedTestHandler handler = new RoomSubjectChangedTestHandler();
		room.addRoomSubjectChangedHandler(handler);
		final Message subject = new Message(null, roomUri, occupantUri);
		subject.setSubject("The subject");
		eventBus.fireEventFromSource(new MessageReceivedEvent(subject), room);
		assertEquals(1, handler.getCalledTimes());
		assertEquals("The subject", handler.getLastEvent().getSubject());
	}
}
