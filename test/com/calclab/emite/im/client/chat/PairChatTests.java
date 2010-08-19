package com.calclab.emite.im.client.chat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.Chat.ChatErrors;
import com.calclab.emite.im.client.chat.Chat.ChatStates;
import com.calclab.emite.xtesting.XmppSessionTester;
import com.calclab.emite.xtesting.handlers.ErrorTestHandler;
import com.calclab.emite.xtesting.handlers.MessageTestHandler;
import com.calclab.emite.xtesting.handlers.StateChangedTestHandler;

/**
 * Pair chat tests using the new Event system
 */
public class PairChatTests {

    private static final XmppURI ME = XmppURI.uri("me@localhost");
    private static final XmppURI CHAT = XmppURI.uri("other@localhost");

    private XmppSessionTester session;
    private ChatProperties properties;
    private PairChat chat;
    private MessageTestHandler beforeSendHandler;
    private MessageTestHandler sentHandler;

    @Before
    public void beforeTests() {
	session = new XmppSessionTester();
	properties = new ChatProperties(CHAT, ME, ChatStates.ready);
	chat = new PairChat(session, properties);
	beforeSendHandler = new MessageTestHandler();
	chat.addBeforeSendMessageHandler(beforeSendHandler);
	sentHandler = new MessageTestHandler();
	chat.addMessageSentHandler(sentHandler);
    }

    @Test
    public void shouldFireChatStateChanges() {
	StateChangedTestHandler handler = new StateChangedTestHandler();
	chat.addChatStateChangedHandler(true, handler);
	assertEquals(1, handler.getCalledTimes());
	chat.setChatState(ChatStates.locked);
	assertEquals(2, handler.getCalledTimes());
    }

    @Test
    public void shouldNotSendOrInterceptOutcomingMessagesIfLocked() {
	properties.setState(ChatStates.locked);
	Message message = new Message("body");
	chat.send(message);
	assertFalse(beforeSendHandler.isCalledOnce());
	assertFalse(sentHandler.isCalledOnce());
    }

    @Test
    public void shouldRaiseErrorIfSendUsingLockedChat() {
	properties.setState(ChatStates.locked);
	ErrorTestHandler handler = new ErrorTestHandler();
	chat.addErrorHandler(handler);
	chat.send(new Message("body"));
	assertTrue(handler.isCalledOnce());
	assertEquals(ChatErrors.sendNotReady, handler.getLastEvent().getErrorType());
    }

    @Test
    public void shouldReceiveAndInterceptIncomingMessages() {
	MessageTestHandler receive = new MessageTestHandler();
	chat.addMessageReceivedHandler(receive);
	MessageTestHandler beforeReceive = new MessageTestHandler();
	chat.addBeforeReceiveMessageHandler(beforeReceive);
	Message message = new Message("body");
	chat.receive(message);
	assertTrue(beforeReceive.isCalledOnce());
	assertSame(message, beforeReceive.getLastMessage());
	assertTrue(receive.isCalledOnce());
	assertSame(message, receive.getLastMessage());
    }

    @Test
    public void shouldSendAndInterceptOutcomingMessagesIfReady() {
	properties.setState(ChatStates.ready);
	Message message = new Message("body");
	chat.send(message);
	assertTrue(beforeSendHandler.isCalledOnce());
	assertTrue(sentHandler.isCalledOnce());
	assertSame(message, beforeSendHandler.getLastMessage());
	assertSame(message, sentHandler.getLastMessage());
    }
}
