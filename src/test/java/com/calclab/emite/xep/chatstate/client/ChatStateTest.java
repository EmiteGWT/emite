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

package com.calclab.emite.xep.chatstate.client;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.PairChat;
import com.calclab.emite.xep.chatstate.client.ChatStateManager.ChatState;
import com.calclab.emite.xtesting.EmiteTestsEventBus;
import com.calclab.emite.xtesting.handlers.ChatStateNotificationTestHandler;

public class ChatStateTest {
	private static final XmppURI MYSELF = uri("self@domain/res");
	private static final XmppURI OTHER = uri("other@domain/otherRes");
	private ChatStateNotificationTestHandler stateHandler;
	private PairChat pairChat;
	private ChatStateManager chatStateManager;

	@Before
	public void aaCreate() {
		pairChat = Mockito.mock(PairChat.class);
		final EmiteEventBus eventBus = new EmiteTestsEventBus("chatEventBus");
		Mockito.when(pairChat.getChatEventBus()).thenReturn(eventBus);
		chatStateManager = new ChatStateManager(pairChat);
		stateHandler = new ChatStateNotificationTestHandler();
		chatStateManager.addChatStateNotificationHandler(stateHandler);
	}

	@Test
	public void shouldFireGone() {
		final Message message = new Message(null, OTHER, MYSELF);
		message.addChild("gone", ChatStateManager.XMLNS);
		chatStateManager.handleMessageReceived(pairChat, message);
		assertEquals(ChatState.gone, stateHandler.getLastChatState());
	}

	@Test
	public void shouldFireOtherCompossing() {
		final Message message = new Message(null, OTHER, MYSELF);
		message.addChild("composing", ChatStateManager.XMLNS);
		chatStateManager.handleMessageReceived(pairChat, message);
		assertEquals(ChatState.composing, stateHandler.getLastChatState());
	}

	@Test
	public void shouldFireOtherCompossingAsGmailDo() {
		final Message message = new Message(null, OTHER, MYSELF);
		message.addChild("cha:composing", ChatStateManager.XMLNS);
		chatStateManager.handleMessageReceived(pairChat, message);
		assertEquals(ChatState.composing, stateHandler.getLastChatState());
	}

	@Test
	public void shouldFireOtherCompossingToWithoutResource() {
		final Message message = new Message(null, OTHER.getJID(), MYSELF);
		message.addChild("cha:composing", ChatStateManager.XMLNS);
		chatStateManager.handleMessageReceived(pairChat, message);
		assertEquals(ChatState.composing, stateHandler.getLastChatState());
	}

}
