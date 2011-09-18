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

package com.calclab.emite.im.client.chat.pair;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.client.stanzas.Message;
import com.calclab.emite.core.client.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.ChatErrors;
import com.calclab.emite.im.client.chat.ChatProperties;
import com.calclab.emite.im.client.chat.ChatStatus;
import com.calclab.emite.im.client.chat.pair.PairChat;
import com.calclab.emite.xtesting.XmppSessionTester;
import com.calclab.emite.xtesting.handlers.BeforeMessageReceivedTestHandler;
import com.calclab.emite.xtesting.handlers.BeforeMessageSentTestHandler;
import com.calclab.emite.xtesting.handlers.ChatStateChangedTestHandler;
import com.calclab.emite.xtesting.handlers.ErrorTestHandler;
import com.calclab.emite.xtesting.handlers.MessageReceivedTestHandler;
import com.calclab.emite.xtesting.handlers.MessageSentTestHandler;

/**
 * Pair chat tests using the new Event system
 */
public class PairChatTests {

	private static final XmppURI ME = XmppURI.uri("me@localhost");
	private static final XmppURI CHAT = XmppURI.uri("other@localhost");

	private XmppSessionTester session;
	private ChatProperties properties;
	private PairChat chat;
	private BeforeMessageSentTestHandler beforeSendHandler;
	private MessageSentTestHandler sentHandler;

	@Before
	public void beforeTests() {
		session = new XmppSessionTester();
		properties = new ChatProperties(CHAT, ME, ChatStatus.ready);
		chat = new PairChat(session, properties);
		beforeSendHandler = new BeforeMessageSentTestHandler();
		chat.addBeforeMessageSentHandler(beforeSendHandler);
		sentHandler = new MessageSentTestHandler();
		chat.addMessageSentHandler(sentHandler);
	}

	@Test
	public void shouldFireChatStateChanges() {
		final ChatStateChangedTestHandler handler = new ChatStateChangedTestHandler();
		chat.addChatStatusChangedHandler(true, handler);
		assertEquals(1, handler.getCalledTimes());
		chat.setStatus(ChatStatus.locked);
		assertEquals(2, handler.getCalledTimes());
	}

	@Test
	public void shouldNotSendOrInterceptOutcomingMessagesIfLocked() {
		properties.setState(ChatStatus.locked);
		final Message message = new Message("body");
		chat.send(message);
		assertFalse(beforeSendHandler.isCalledOnce());
		assertFalse(sentHandler.isCalledOnce());
	}

	@Test
	public void shouldRaiseErrorIfSendUsingLockedChat() {
		properties.setState(ChatStatus.locked);
		final ErrorTestHandler handler = new ErrorTestHandler();
		chat.addErrorHandler(handler);
		chat.send(new Message("body"));
		assertTrue(handler.isCalledOnce());
		assertEquals(ChatErrors.sendNotReady, handler.getLastEvent().getErrorType());
	}

	@Test
	public void shouldReceiveAndInterceptIncomingMessages() {
		final MessageReceivedTestHandler receive = new MessageReceivedTestHandler();
		chat.addMessageReceivedHandler(receive);
		final BeforeMessageReceivedTestHandler beforeReceive = new BeforeMessageReceivedTestHandler();
		chat.addBeforeMessageReceivedHandler(beforeReceive);
		final Message message = new Message("body");
		chat.receive(message);
		assertTrue(beforeReceive.isCalledOnce());
		assertSame(message, beforeReceive.getLastMessage());
		assertTrue(receive.isCalledOnce());
		assertSame(message, receive.getLastMessage());
	}

	@Test
	public void shouldSendAndInterceptOutcomingMessagesIfReady() {
		properties.setState(ChatStatus.ready);
		final Message message = new Message("body");
		chat.send(message);
		assertTrue(beforeSendHandler.isCalledOnce());
		assertTrue(sentHandler.isCalledOnce());
		assertSame(message, beforeSendHandler.getLastMessage());
		assertSame(message, sentHandler.getLastMessage());
	}
}
