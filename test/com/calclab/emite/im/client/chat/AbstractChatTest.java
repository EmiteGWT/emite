package com.calclab.emite.im.client.chat;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.Chat.State;
import com.calclab.emite.xtesting.SessionTester;
import com.calclab.emite.xtesting.XmppSessionTester;
import com.calclab.suco.testing.events.MockedListener;

public abstract class AbstractChatTest {
    protected final SessionTester session;
    protected XmppSessionTester xmppSession;
    protected static final XmppURI USER_URI = uri("self@domain/res");

    public AbstractChatTest() {
	xmppSession = new XmppSessionTester();
	session = new SessionTester(xmppSession);
    }

    public abstract AbstractChat getChat();

    @Test
    public void shouldInterceptIncomingMessages() {
	final AbstractChat chat = getChat();
	final MockedListener<Message> interceptor = new MockedListener<Message>();
	chat.onBeforeReceive(interceptor);
	final Message message = new Message("body", chat.getURI());
	session.receives(message);
	assertTrue(interceptor.isCalledWithSame(message));
    }

    @Test
    public void shouldInterceptOutcomingMessages() {
	final AbstractChat chat = getChat();
	final MockedListener<Message> interceptor = new MockedListener<Message>();
	chat.onBeforeSend(interceptor);
	final Message message = new Message("body");
	chat.send(message);
	assertTrue(interceptor.isCalledWithSame(message));
    }

    @Test
    public void shouldNotSendMessagesWhenStatusIsNotReady() {
	final AbstractChat chat = getChat();
	chat.setState(State.locked);
	chat.send(new Message("a message"));
	session.verifyNotSent("<message />");
    }

    @Test
    public void shouldSetNullData() {
	getChat().setData(null, null);
    }
}
