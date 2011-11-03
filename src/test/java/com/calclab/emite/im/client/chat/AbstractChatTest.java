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

import static com.calclab.emite.core.client.uri.XmppURI.uri;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.calclab.emite.core.client.stanzas.Message;
import com.calclab.emite.core.client.uri.XmppURI;
import com.calclab.emite.xtesting.XmppSessionTester;
import com.calclab.emite.xtesting.handlers.BeforeMessageReceivedTestHandler;
import com.calclab.emite.xtesting.handlers.BeforeMessageSentTestHandler;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;

public abstract class AbstractChatTest<C extends AbstractChat> {
	protected final EventBus eventBus;
	protected final XmppSessionTester session;
	protected static final XmppURI USER_URI = uri("self@domain/res");

	public AbstractChatTest() {
		eventBus = new SimpleEventBus();
		session = new XmppSessionTester();
	}

	public abstract C getChat();

	@Test
	public void shouldInterceptIncomingMessages() {
		final C chat = getChat();
		final BeforeMessageReceivedTestHandler interceptor = new BeforeMessageReceivedTestHandler();
		chat.addBeforeMessageReceivedHandler(interceptor);
		final Message message = new Message("body", USER_URI, chat.getURI());
		session.receives(message);
		assertEquals(message, interceptor.getLastMessage());
	}

	@Test
	public void shouldInterceptOutcomingMessages() {
		final C chat = getChat();
		chat.setStatus(ChatStatus.ready);
		final BeforeMessageSentTestHandler interceptor = new BeforeMessageSentTestHandler();
		chat.addBeforeMessageSentHandler(interceptor);
		final Message message = new Message("body");
		chat.send(message);
		assertEquals(message, interceptor.getLastMessage());
	}

	@Test
	public void shouldNotSendMessagesWhenStatusIsNotReady() {
		final C chat = getChat();
		chat.setStatus(ChatStatus.locked);
		chat.send(new Message("a message"));
		session.verifyNotSent("<message />");
	}

	@Test
	public void shouldSetNullData() {
		getChat().getProperties().setData(null, null);
	}
}
