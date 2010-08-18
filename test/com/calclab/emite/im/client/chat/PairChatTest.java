package com.calclab.emite.im.client.chat;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.Chat.ChatStates;
import com.calclab.emite.im.client.chat.Chat.State;
import com.calclab.suco.testing.events.MockedListener;

public class PairChatTest extends AbstractChatTest {
    private static final XmppURI CHAT_URI = uri("other@domain/other");
    private PairChat pairChat;

    @Before
    public void beforeTests() {
	xmppSession.setLoggedIn(USER_URI);
	PairChatManager manager = new PairChatManager(xmppSession);
	final ChatProperties properties = new ChatProperties(CHAT_URI, USER_URI, ChatStates.ready);
	pairChat = (PairChat) manager.openChat(properties, true);
	pairChat.setThread("theThread");
    }

    @Override
    public AbstractChat getChat() {
	return pairChat;
    }

    @Test
    public void shouldBeReadyIfSessionLogedIn() {
	final ChatProperties properties = new ChatProperties(uri("someone@domain"), USER_URI, ChatStates.ready);
	final PairChat aChat = new PairChat(xmppSession, properties);
	assertEquals(Chat.State.ready, aChat.getState());
    }

    @Test
    public void shouldLockIfLogoutAndUnlockWhenLogginWithSameUser() {
	final MockedListener<State> listener = new MockedListener<State>();
	pairChat.onStateChanged(listener);
	assertEquals(Chat.State.ready, pairChat.getState());
	xmppSession.logout();
	assertTrue(listener.isCalledWithSame(Chat.State.locked));
	listener.clear();
	xmppSession.login(USER_URI, "");
	assertTrue(listener.isCalledWithSame(Chat.State.ready));
    }

    @Test
    public void shouldLockIfReLoginWithDifferentJID() {
	xmppSession.logout();
	xmppSession.login(uri("differentUser@domain"), "");
	assertEquals(Chat.State.locked, pairChat.getState());

    }

    @Test
    public void shouldReceiveMessages() {
	final MockedListener<Message> messageReceived = new MockedListener<Message>();
	pairChat.onMessageReceived(messageReceived);
	xmppSession.receives(new Message("the body", USER_URI, CHAT_URI));
	assertTrue("should receive messages", messageReceived.isCalled(1));
    }

    @Test
    public void shouldSendNoThreadWhenNotSpecified() {
	final ChatProperties properties = new ChatProperties(CHAT_URI, USER_URI, null);
	final AbstractChat noThreadChat = new PairChat(xmppSession, properties);
	noThreadChat.setState(State.ready);
	noThreadChat.send(new Message("the message"));
	xmppSession.verifySent("<message from='self@domain/res' to='other@domain/other' "
		+ "type='chat'><body>the message</body></message>");
    }

    @Test
    public void shouldSendThreadWhenSpecified() {
	pairChat.send(new Message("the message"));
	xmppSession.verifySent("<message from='self@domain/res' to='other@domain/other' type='chat'>"
		+ "<body>the message</body><thread>theThread</thread></message>");
    }

    @Test
    public void shouldSendValidChatMessages() {
	pairChat.send(new Message("this is the body", uri("to@uri"), uri("from@uri")).Thread("otherThread").Type(
		Message.Type.groupchat));
	xmppSession.verifySent("<message from='self@domain/res' to='other@domain/other' type='chat'>"
		+ "<body>this is the body</body><thread>theThread</thread></message>");
    }

    @Test
    public void shouldUnlockIfReloginWithSameJID() {
	xmppSession.logout();
	xmppSession.login(XmppURI.uri(USER_URI.getNode(), USER_URI.getHost(), "different_resource"), "");
	assertEquals(Chat.State.ready, pairChat.getState());
    }

    @Test
    public void shoultEscapeMessageBody() {
	pairChat.send(new Message("&"));
	xmppSession.verifySent("<message from='self@domain/res' to='other@domain/other' type='chat'>"
		+ "<body>&amp;</body><thread>theThread</thread></message>");
    }

}
