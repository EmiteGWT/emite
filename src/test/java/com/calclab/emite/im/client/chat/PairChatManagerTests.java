package com.calclab.emite.im.client.chat;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.xtesting.XmppSessionTester;
import com.calclab.emite.xtesting.handlers.MessageTestHandler;

public class PairChatManagerTests extends AbstractChatManagerTests {

	@Test
	public void shouldBeInitiatedByOtherIfMessageArrives() {
		session.receives(new Message("body", ME, OTHER));
		final Chat chat = manager.open(uri("someone@domain"));
		assertFalse(chat.isInitiatedByMe());
	}

	@Test
	public void shouldForwardMessagesToChats() {
		final Chat chat = manager.open(OTHER);
		final MessageTestHandler handler = new MessageTestHandler();
		chat.addMessageReceivedHandler(handler);
		session.receives(new Message("body", ME, OTHER));
		assertEquals(1, handler.getCalledTimes());
		session.receives("<message type='chat' id='purplee8b92642' to='me@localhost' " + "from='other@localhost'><x xmlns='jabber:x:event'/><active"
				+ "xmlns='http://jabber.org/protocol/chatstates'/></message>");
		assertEquals(2, handler.getCalledTimes());
	}

	@Test
	public void shouldOpenChatWhenLogin() {
		session.logout();
		final Chat chat = manager.open(OTHER);
		assertEquals(ChatStates.locked, chat.getChatState());
		session.setLoggedIn(ME);
		assertEquals(ChatStates.ready, chat.getChatState());
	}

	@Test
	public void shouldReturnLockedChatsIfNotLoggedIn() {
		session.logout();
		final Chat chat = manager.open(OTHER);
		assertEquals(ChatStates.locked, chat.getChatState());
	}

	@Test
	public void shouldReturnReadyChatsIfLoggedIn() {
		session.login(ME, "");
		final Chat chat = manager.open(OTHER);
		assertEquals(ChatStates.ready, chat.getChatState());
	}

	@Override
	protected PairChatManager createChatManager(final XmppSessionTester session2) {
		return new PairChatManager(session);
	}

}
