package com.calclab.emite.im.client.chat;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.xtesting.XmppSessionTester;
import com.calclab.emite.xtesting.handlers.MessageTestHandler;

public abstract class AbstractChatTest {
	protected final XmppSessionTester session;
	protected static final XmppURI USER_URI = uri("self@domain/res");

	public AbstractChatTest() {
		session = new XmppSessionTester();
	}

	public abstract AbstractChat getChat();

	@Test
	public void shouldInterceptIncomingMessages() {
		final AbstractChat chat = getChat();
		final MessageTestHandler interceptor = new MessageTestHandler();
		chat.addBeforeReceiveMessageHandler(interceptor);
		final Message message = new Message("body", USER_URI, chat.getURI());
		session.receives(message);
		assertEquals(message, interceptor.getLastMessage());
	}

	@Test
	public void shouldInterceptOutcomingMessages() {
		final AbstractChat chat = getChat();
		chat.setChatState(ChatStates.ready);
		final MessageTestHandler interceptor = new MessageTestHandler();
		chat.addBeforeSendMessageHandler(interceptor);
		final Message message = new Message("body");
		chat.send(message);
		assertEquals(message, interceptor.getLastMessage());
	}

	@Test
	public void shouldNotSendMessagesWhenStatusIsNotReady() {
		final AbstractChat chat = getChat();
		chat.setChatState("locked");
		chat.send(new Message("a message"));
		session.verifyNotSent("<message />");
	}

	@Test
	public void shouldSetNullData() {
		getChat().setData(null, null);
	}
}
