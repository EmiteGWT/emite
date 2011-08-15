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

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.client.xmpp.session.SessionStates;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.xtesting.XmppSessionTester;

public abstract class AbstractChatManagerTests {
	protected static final XmppURI ME = XmppURI.uri("me@localhost");
	protected static final XmppURI OTHER = XmppURI.uri("other@localhost");

	protected XmppSessionTester session;
	protected PairChatManager manager;

	@Before
	public void beforeTests() {
		session = new XmppSessionTester();
		manager = createChatManager(session);
	}

	@Test
	public void shouldCloseChatsWhenDisconnected() {
		session.setLoggedIn(ME);
		final Chat chat = manager.open(OTHER);
		assertEquals(ChatStates.ready, chat.getChatState());
		session.setSessionState(SessionStates.disconnected);
		assertEquals(ChatStates.locked, chat.getChatState());
	}

	@Test
	public void shouldCloseChatsWhenLogout() {
		session.setLoggedIn(ME);
		final Chat chat = manager.open(OTHER);
		assertEquals(ChatStates.ready, chat.getChatState());
		session.logout();
		assertEquals(ChatStates.locked, chat.getChatState());

	}

	protected abstract PairChatManager createChatManager(XmppSessionTester session2);
}
