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

import static com.calclab.emite.core.client.stanzas.XmppURI.uri;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.client.stanzas.Message;
import com.calclab.emite.core.client.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.pair.PairChat;
import com.calclab.emite.im.client.chat.pair.PairChatManagerImpl;
import com.calclab.emite.im.client.chat.pair.PairChatSelectionStrategy;
import com.calclab.emite.xep.chatstate.client.ChatStateHook.ChatState;
import com.calclab.emite.xtesting.XmppSessionTester;
import com.calclab.emite.xtesting.handlers.ChatStateChangedTestHandler;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;

public class ChatStateManagerTest {
	private static final XmppURI MYSELF = uri("self@domain/res");
	private static final XmppURI OTHER = uri("other@domain/other");

	private PairChatManagerImpl chatManager;
	private ChatStateChangedTestHandler stateHandler;
	private PairChat chat;
	private ChatStateHook chatStateManager;
	private XmppSessionTester session;

	@Before
	public void beforeTests() {
		final EventBus eventBus = new SimpleEventBus();
		session = new XmppSessionTester();
		chatManager = new PairChatManagerImpl(eventBus, session, new PairChatSelectionStrategy());
		session.setLoggedIn(MYSELF);
		final ChatStateManager stateManager = new ChatStateManager(eventBus, chatManager);
		chat = chatManager.open(OTHER);
		chatStateManager = stateManager.getChatState(chat);
		stateHandler = new ChatStateChangedTestHandler();
		chatStateManager.addChatStateChangedHandler(stateHandler);
	}

	@Test
	public void closeChatWithoutStartConversationMustNotThrowNPE() {
		// This was throwing a NPE:
		chatManager.close(chat);
	}

	@Test
	public void shouldNotRepiteState() {
		session.receives("<message from='other@domain/other' to='self@domain/res' type='chat'>"
				+ "<active xmlns='http://jabber.org/protocol/chatstates'/></message>");
		chatStateManager.setOwnState(ChatState.composing);
		chatStateManager.setOwnState(ChatState.composing);
		chatStateManager.setOwnState(ChatState.composing);
		session.verifySent("<message from='self@domain/res' to='other@domain/other' type='chat'>"
				+ "<composing xmlns='http://jabber.org/protocol/chatstates'/></message>");
		session.verifyNotSent("<message><composing/><active/></message>");
	}

	@Test
	public void shouldNotSendStateIfNegotiationNotAccepted() {
		chatStateManager.setOwnState(ChatState.composing);
		session.verifySentNothing();
	}

	@Test
	public void shouldSendActiveIfTheOtherStartNegotiation() {
		session.receives("<message from='other@domain/other' to='self@domain/res' type='chat'>"
				+ "<active xmlns='http://jabber.org/protocol/chatstates'/></message>");
		session.verifySent("<message from='self@domain/res' to='other@domain/other' type='chat'>"
				+ "<active xmlns='http://jabber.org/protocol/chatstates'/></message>");
	}

	@Test
	public void shouldSendStateIfNegotiationAccepted() {
		session.receives("<message from='other@domain/other' to='self@domain/res' type='chat'>"
				+ "<active xmlns='http://jabber.org/protocol/chatstates'/></message>");
		chatStateManager.setOwnState(ChatState.composing);
		session.verifySent("<message from='self@domain/res' to='other@domain/other' type='chat'>"
				+ "<composing xmlns='http://jabber.org/protocol/chatstates'/></message>");
	}

	@Test
	public void shouldSendTwoStateIfDiferent() {
		session.receives("<message from='other@domain/other' to='self@domain/res' type='chat'>"
				+ "<active xmlns='http://jabber.org/protocol/chatstates'/></message>");
		chatStateManager.setOwnState(ChatState.composing);
		chatStateManager.setOwnState(ChatState.pause);
		session.verifySent("<message from='self@domain/res' to='other@domain/other' type='chat'>"
				+ "<composing xmlns='http://jabber.org/protocol/chatstates'/></message>"
				+ "<message from='self@domain/res' to='other@domain/other' type='chat'>" + "<pause xmlns='http://jabber.org/protocol/chatstates'/></message>");
	}

	@Test
	public void shouldStartStateAfterNegotiation() {
		chat.send(new Message("test message"));
		session.receives("<message from='other@domain/other' to='self@domain/res' type='chat'>"
				+ "<active xmlns='http://jabber.org/protocol/chatstates'/></message>");
		final Message message = new Message("test message", OTHER, MYSELF);
		message.addChild(ChatStateHook.ChatState.active.toString(), ChatStateHook.XMLNS);
		chatStateManager.setOwnState(ChatStateHook.ChatState.composing);
		session.verifySent(message.toString() + "<message from='self@domain/res' to='other@domain/other' type='chat'>"
				+ "<composing xmlns='http://jabber.org/protocol/chatstates'/></message>");
	}

	@Test
	public void shouldStartStateNegotiation() {
		chat.send(new Message("test message"));
		chat.send(new Message("test message"));
		session.verifySent("<message><active xmlns='http://jabber.org/protocol/chatstates' /></message>");
	}

	@Test
	public void shouldStartStateNegotiationOnce() {
		chat.send(new Message("message1"));
		chat.send(new Message("message2"));
		session.verifySent("<message><body>message1</body><active /></message>");
		session.verifySent("<message><body>message2</body></message>");
		session.verifyNotSent("<message><body>message2</body><active /></message>");
	}

	@Test
	public void shouldStopAfterGone() {
		session.receives("<message from='other@domain/other' to='self@domain/res' type='chat'><active /></message>");
		session.receives("<message from='other@domain/other' to='self@domain/res' type='chat'><gone /></message>");
		chatStateManager.setOwnState(ChatStateHook.ChatState.composing);
		chatStateManager.setOwnState(ChatStateHook.ChatState.pause);
		session.verifySent("<message><active /></message>");
		session.verifyNotSent("<message><composing /></message>");
		session.verifyNotSent("<message><pause /></message>");
		session.verifyNotSent("<message><gone /></message>");
	}

}
