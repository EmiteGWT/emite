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

package com.calclab.emite.im.chat.pair;

import static com.calclab.emite.core.XmppURI.uri;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import com.calclab.emite.core.stanzas.Message;
import com.calclab.emite.im.chat.AbstractChatManagerTests;
import com.calclab.emite.im.chat.Chat;
import com.calclab.emite.im.chat.ChatStatus;
import com.calclab.emite.im.chat.PairChatManagerImpl;
import com.calclab.emite.xtesting.XmppSessionTester;
import com.calclab.emite.xtesting.handlers.MessageReceivedTestHandler;
import com.google.web.bindery.event.shared.SimpleEventBus;

public class PairChatManagerTests extends AbstractChatManagerTests {

	@Test
	public void shouldBeInitiatedByOtherIfMessageArrives() {
		session.receives(new Message("body", ME, OTHER));
		final Chat chat = manager.open(uri("someone@domain"));
		assertFalse(chat.isInitiatedByMe());
	}

	@Test
	public void shouldForwardMessagesToChats() {
		final Chat chat = manager.open(OTHER);
		final MessageReceivedTestHandler handler = new MessageReceivedTestHandler();
		chat.addMessageReceivedHandler(handler);
		session.receives(new Message("body", ME, OTHER));
		assertEquals(1, handler.getCalledTimes());
		session.receives("<message type='chat' id='purplee8b92642' to='me@localhost' " + "from='other@localhost'><x xmlns='jabber:x:event'/><active"
				+ "xmlns='http://jabber.org/protocol/chatstates'/></message>");
		assertEquals(2, handler.getCalledTimes());
	}

	@Test
	public void shouldOpenChatWhenLogin() {
		session.logout();
		final Chat chat = manager.open(OTHER);
		assertEquals(ChatStatus.locked, chat.getStatus());
		session.setLoggedIn(ME);
		assertEquals(ChatStatus.ready, chat.getStatus());
	}

	@Test
	public void shouldReturnLockedChatsIfNotLoggedIn() {
		session.logout();
		final Chat chat = manager.open(OTHER);
		assertEquals(ChatStatus.locked, chat.getStatus());
	}

	@Test
	public void shouldReturnReadyChatsIfLoggedIn() {
		session.login(ME, "");
		final Chat chat = manager.open(OTHER);
		assertEquals(ChatStatus.ready, chat.getStatus());
	}

	@Override
	protected PairChatManagerImpl createChatManager(final XmppSessionTester session2) {
		return new PairChatManagerImpl(new SimpleEventBus(), session, new PairChatSelectionStrategy());
	}

}
