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

package com.calclab.emite.im.client.chat;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.xtesting.XmppSessionTester;
import com.calclab.emite.xtesting.handlers.MessageTestHandler;

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
		final MessageTestHandler handler = new MessageTestHandler();
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
		assertEquals(ChatStates.locked, chat.getChatState());
		session.setLoggedIn(ME);
		assertEquals(ChatStates.ready, chat.getChatState());
	}

	@Test
	public void shouldReturnLockedChatsIfNotLoggedIn() {
		session.logout();
		final Chat chat = manager.open(OTHER);
		assertEquals(ChatStates.locked, chat.getChatState());
	}

	@Test
	public void shouldReturnReadyChatsIfLoggedIn() {
		session.login(ME, "");
		final Chat chat = manager.open(OTHER);
		assertEquals(ChatStates.ready, chat.getChatState());
	}

	@Override
	protected PairChatManager createChatManager(final XmppSessionTester session2) {
		return new PairChatManager(session);
	}

}
