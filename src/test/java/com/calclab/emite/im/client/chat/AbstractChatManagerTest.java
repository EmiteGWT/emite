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
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.xtesting.XmppSessionTester;
import com.calclab.emite.xtesting.handlers.ChatChangedTestHandler;

public abstract class AbstractChatManagerTest {
	protected static final XmppURI MYSELF = uri("self@domain");
	protected static final XmppURI OTHER = uri("other@domain");
	protected ChatManager manager;
	protected XmppSessionTester session;

	@Before
	public void beforeTests() {
		session = new XmppSessionTester();
		manager = createChatManager();
		session.login(MYSELF, null);
	}

	@Test
	public void shouldBeInitiatedByMeIfIOpenAChat() {
		final Chat chat = manager.open(uri("other@domain/resource"));
		assertTrue(chat.isInitiatedByMe());
	}

	@Test
	public void shouldEventWhenAChatIsClosed() {
		final Chat chat = manager.open(uri("other@domain/resource"));
		final ChatChangedTestHandler handler = new ChatChangedTestHandler("closed");
		manager.addChatChangedHandler(handler);
		manager.close(chat);
		assertTrue(handler.isCalledOnce());
	}

	@Test
	public void shouldEventWhenChatCreated() {
		final ChatChangedTestHandler handler = new ChatChangedTestHandler("created");
		manager.addChatChangedHandler(handler);
		manager.open(OTHER);
		assertTrue(handler.isCalledOnce());
	}

	protected abstract ChatManager createChatManager();
}
