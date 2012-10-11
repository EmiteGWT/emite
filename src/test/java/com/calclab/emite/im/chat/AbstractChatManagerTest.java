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

import static com.calclab.emite.core.XmppURI.uri;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.XmppURI;
import com.calclab.emite.xtesting.XmppSessionTester;

public abstract class AbstractChatManagerTest<M extends ChatManager<C>, C extends Chat> {
	protected static final XmppURI MYSELF = uri("self@domain");
	protected static final XmppURI OTHER = uri("other@domain");
	protected XmppSessionTester session;
	protected M manager;

	@Before
	public void beforeTests() {
		session = new XmppSessionTester();
		manager = createChatManager();
		session.login(MYSELF, null);
	}

	@Test
	public void shouldBeInitiatedByMeIfIOpenAChat() {
		final C chat = manager.open(uri("other@domain/resource"));
		assertTrue(chat.isInitiatedByMe());
	}

	protected abstract M createChatManager();
}
