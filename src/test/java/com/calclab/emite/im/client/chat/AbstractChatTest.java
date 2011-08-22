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

import org.junit.Test;

import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.xtesting.XmppSessionTester;
import com.calclab.emite.xtesting.handlers.BeforeMessageReceivedTestHandler;
import com.calclab.emite.xtesting.handlers.BeforeMessageSentTestHandler;

public abstract class AbstractChatTest {
	protected final XmppSessionTester session;
	protected static final XmppURI USER_URI = uri("self@domain/res");

	public AbstractChatTest() {
		session = new XmppSessionTester();
	}

	public abstract ChatBoilerplate getChat();

	@Test
	public void shouldInterceptIncomingMessages() {
		final ChatBoilerplate chat = getChat();
		final BeforeMessageReceivedTestHandler interceptor = new BeforeMessageReceivedTestHandler();
		chat.addBeforeMessageReceivedHandler(interceptor);
		final Message message = new Message("body", USER_URI, chat.getURI());
		session.receives(message);
		assertEquals(message, interceptor.getLastMessage());
	}

	@Test
	public void shouldInterceptOutcomingMessages() {
		final ChatBoilerplate chat = getChat();
		chat.setChatState(ChatStates.ready);
		final BeforeMessageSentTestHandler interceptor = new BeforeMessageSentTestHandler();
		chat.addBeforeMessageSentHandler(interceptor);
		final Message message = new Message("body");
		chat.send(message);
		assertEquals(message, interceptor.getLastMessage());
	}

	@Test
	public void shouldNotSendMessagesWhenStatusIsNotReady() {
		final ChatBoilerplate chat = getChat();
		chat.setChatState("locked");
		chat.send(new Message("a message"));
		session.verifyNotSent("<message />");
	}

	@Test
	public void shouldSetNullData() {
		getChat().getProperties().setData(null, null);
	}
}
