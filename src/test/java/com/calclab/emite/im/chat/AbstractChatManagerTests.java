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

package com.calclab.emite.im.chat;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.XmppURI;
import com.calclab.emite.core.session.SessionStatus;
import com.calclab.emite.im.chat.PairChatManagerImpl;
import com.calclab.emite.xtesting.XmppSessionTester;

public abstract class AbstractChatManagerTests {
	protected static final XmppURI ME = XmppURI.uri("me@localhost");
	protected static final XmppURI OTHER = XmppURI.uri("other@localhost");

	protected XmppSessionTester session;
	protected PairChatManagerImpl manager;

	@Before
	public void beforeTests() {
		session = new XmppSessionTester();
		manager = createChatManager(session);
	}

	@Test
	public void shouldCloseChatsWhenDisconnected() {
		session.setLoggedIn(ME);
		final Chat chat = manager.open(OTHER);
		assertEquals(ChatStatus.ready, chat.getStatus());
		session.setStatus(SessionStatus.disconnected);
		assertEquals(ChatStatus.locked, chat.getStatus());
	}

	@Test
	public void shouldCloseChatsWhenLogout() {
		session.setLoggedIn(ME);
		final Chat chat = manager.open(OTHER);
		assertEquals(ChatStatus.ready, chat.getStatus());
		session.logout();
		assertEquals(ChatStatus.locked, chat.getStatus());

	}

	protected abstract PairChatManagerImpl createChatManager(XmppSessionTester session2);
}
