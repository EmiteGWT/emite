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

import static com.calclab.emite.core.client.stanzas.XmppURI.uri;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.calclab.emite.core.client.stanzas.Message;
import com.calclab.emite.im.client.chat.AbstractChatManagerTest;
import com.calclab.emite.im.client.chat.ChatStatus;
import com.calclab.emite.im.client.chat.pair.PairChatManagerImpl;
import com.calclab.emite.xep.chatstate.client.ChatStateHook;
import com.calclab.emite.xtesting.handlers.ChatChangedTestHandler;
import com.calclab.emite.xtesting.handlers.ChatStatusChangedTestHandler;
import com.calclab.emite.xtesting.handlers.MessageReceivedTestHandler;
import com.google.web.bindery.event.shared.SimpleEventBus;

public class PairChatManagerTest extends AbstractChatManagerTest<PairChat> {

	@Test
	public void chatStateDontFireOnChatCreatedButMustAfterOpenChat() {
		final Message message = new Message(null, MYSELF, OTHER);
		message.addChild("gone", ChatStateHook.XMLNS);

		final ChatChangedTestHandler handler = addChatCreatedHandler();
		session.receives(message);
		assertTrue(handler.isNotCalled());
		manager.open(OTHER);
		assertTrue(handler.isCalled());
	}

	@Test
	public void managerShouldCreateOneChatForSameResource() {
		final ChatChangedTestHandler handler = addChatCreatedHandler();
		session.receives(new Message("message 1", MYSELF, uri("source@domain/resource1")));
		session.receives(new Message("message 2", MYSELF, uri("source@domain/resource1")));
		assertEquals(1, handler.getCalledTimes());
	}

	@Test
	public void oneToOneChatsAreAlwaysReadyWhenCreated() {
		final PairChat chat = manager.open(uri("other@domain/resource"));
		assertEquals(ChatStatus.ready, chat.getStatus());
	}

	@Test
	public void roomInvitationsShouldDontFireOnChatCreated() {
		final ChatChangedTestHandler handler = addChatCreatedHandler();
		session.receives("<message to='" + MYSELF + "' from='someroom@domain'><x xmlns='http://jabber.org/protocol/muc#user'>" + "<invite from='" + OTHER
				+ "'><reason>Join to our conversation</reason></invite>" + "</x><x jid='someroom@domain' xmlns='jabber:x:conference' /></message>");
		assertTrue(handler.isNotCalled());
	}

	@Test
	public void roomInvitationsShouldDontFireOnChatCreatedButMustAfterOpenChat() {
		final ChatChangedTestHandler handler = addChatCreatedHandler();
		session.receives("<message to='" + MYSELF + "' from='someroom@domain'><x xmlns='http://jabber.org/protocol/muc#user'>" + "<invite from='" + OTHER
				+ "'><reason>Join to our conversation</reason></invite>" + "</x><x jid='someroom@domain' xmlns='jabber:x:conference' /></message>");
		assertTrue(handler.isNotCalled());
		manager.open(OTHER);
		assertTrue(handler.isCalled());
	}

	@Test
	public void shouldBeInitiatedByOtherIfMessageArrives() {
		session.receives("<message to='" + MYSELF + "' from='someone@domain'><body>the body</body></message>");
		final PairChat chat = manager.open(uri("someone@domain"));
		assertFalse(chat.isInitiatedByMe());
	}

	@Test
	public void shouldBlockChatWhenClosingIt() {
		final PairChat chat = manager.open(uri("other@domain/resource"));
		assertEquals(ChatStatus.ready, chat.getStatus());
		manager.close(chat);
		assertEquals(ChatStatus.locked, chat.getStatus());
	}

	@Test
	public void shouldCloseChatWhenLoggedOut() {
		final PairChat chat = manager.open(uri("name@domain/resouce"));
		assertEquals(ChatStatus.ready, chat.getStatus());
		final ChatStatusChangedTestHandler handler = new ChatStatusChangedTestHandler();
		chat.addChatStatusChangedHandler(false, handler);
		session.logout();
		assertEquals(ChatStatus.locked, handler.getLastChatStatus());
	}

	@Test
	public void shouldEventIncommingMessages() {
		final PairChat chat = manager.open(uri("someone@domain"));
		final MessageReceivedTestHandler handler = new MessageReceivedTestHandler();
		chat.addMessageReceivedHandler(handler);
		session.receives("<message type='chat' id='purplee8b92642' to='user@domain' " + "from='someone@domain'><x xmlns='jabber:x:event'/><active"
				+ "xmlns='http://jabber.org/protocol/chatstates'/></message>");
		assertTrue(handler.isCalledOnce());
	}

	@Test
	public void shouldOpenDifferentsChatsForDifferentDomains() {
		final PairChat chatCom = manager.open(uri("COM@domain.com"));
		final MessageReceivedTestHandler handlerCom = new MessageReceivedTestHandler();
		chatCom.addMessageReceivedHandler(handlerCom);
		assertTrue("com listener empty", handlerCom.isNotCalled());

		final PairChat chatOrg = manager.open(uri("ORG@domain.org"));
		final MessageReceivedTestHandler handlerOrg = new MessageReceivedTestHandler();
		chatOrg.addMessageReceivedHandler(handlerOrg);
		assertTrue("org listener empty", handlerCom.isNotCalled());

		session.receives(new Message("message com 2", MYSELF, uri("COM@domain.com")));
		assertTrue("com has one message", handlerCom.isCalledOnce());
		assertTrue("org has no message", handlerOrg.isNotCalled());

	}

	@Test
	public void shouldReuseChatIfNotResouceSpecified() {
		final ChatChangedTestHandler handler = addChatCreatedHandler();
		session.receives(new Message("message 1", MYSELF, uri("source@domain")));
		session.receives(new Message("message 2", MYSELF, uri("source@domain/resource1")));
		assertTrue(handler.isCalledOnce());
	}

	@Test
	public void shouldReuseChatWhenAnsweringWithDifferentResources() {
		final ChatChangedTestHandler handler = addChatCreatedHandler();
		final PairChat chat = manager.open(uri("someone@domain"));
		assertTrue(handler.isCalledOnce());
		assertEquals(chat, handler.getLastChat());
		session.receives(new Message("answer", MYSELF, uri("someone@domain/resource")));
		assertTrue(handler.isCalledOnce());
	}

	private ChatChangedTestHandler addChatCreatedHandler() {
		final ChatChangedTestHandler handler = new ChatChangedTestHandler("created");
		manager.addPairChatChangedHandler(handler);
		return handler;
	}

	@Override
	protected PairChatManagerImpl createChatManager() {
		final PairChatManagerImpl chatManagerDefault = new PairChatManagerImpl(new SimpleEventBus(), session, new PairChatSelectionStrategy());
		return chatManagerDefault;
	}
}
