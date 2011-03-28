package com.calclab.emite.im.client.chat;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.xtesting.handlers.MessageTestHandler;
import com.calclab.emite.xtesting.handlers.StateChangedTestHandler;

/**
 * Pair chat tests using the old listener interface
 */
public class PairChatTest extends AbstractChatTest {
    private static final XmppURI CHAT_URI = uri("other@domain/other");
    private PairChat pairChat;

    @Before
    public void beforeTests() {
	session.setLoggedIn(USER_URI);
	PairChatManager manager = new PairChatManager(session);
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
	final PairChat aChat = new PairChat(session, properties);
	assertEquals("ready", aChat.getChatState());
    }

    @Test
    public void shouldLockIfLogoutAndUnlockWhenLogginWithSameUser() {
	final StateChangedTestHandler handler = new StateChangedTestHandler();
	pairChat.addChatStateChangedHandler(true, handler);
	assertEquals("ready", handler.getLastState());
	session.logout();
	assertEquals("locked", handler.getLastState());
	session.login(USER_URI, "");
	assertEquals("ready", handler.getLastState());
    }

    @Test
    public void shouldLockIfReLoginWithDifferentJID() {
	session.logout();
	session.login(uri("differentUser@domain"), "");
	assertEquals("locked", pairChat.getChatState());

    }

    @Test
    public void shouldReceiveMessages() {
	final MessageTestHandler handler = new MessageTestHandler();
	pairChat.addMessageReceivedHandler(handler);
	session.receives(new Message("the body", USER_URI, CHAT_URI));
	assertTrue("should receive messages", handler.isCalledOnce());
    }

    @Test
    public void shouldSendNoThreadWhenNotSpecified() {
	final ChatProperties properties = new ChatProperties(CHAT_URI, USER_URI, null);
	final AbstractChat noThreadChat = new PairChat(session, properties);
	noThreadChat.setChatState("ready");
	noThreadChat.send(new Message("the message"));
	session.verifySent("<message from='self@domain/res' to='other@domain/other' "
		+ "type='chat'><body>the message</body></message>");
    }

    @Test
    public void shouldSendThreadWhenSpecified() {
	pairChat.send(new Message("the message"));
	session.verifySent("<message from='self@domain/res' to='other@domain/other' type='chat'>"
		+ "<body>the message</body><thread>theThread</thread></message>");
    }

    @Test
    public void shouldSendValidChatMessages() {
	pairChat.send(new Message("this is the body", uri("to@uri"), uri("from@uri")).Thread("otherThread").Type(
		Message.Type.groupchat));
	session.verifySent("<message from='self@domain/res' to='other@domain/other' type='chat'>"
		+ "<body>this is the body</body><thread>theThread</thread></message>");
    }

    @Test
    public void shouldUnlockIfReloginWithSameJID() {
	session.logout();
	session.login(XmppURI.uri(USER_URI.getNode(), USER_URI.getHost(), "different_resource"), "");
	assertEquals("ready", pairChat.getChatState());
    }

    @Test
    public void shoultEscapeMessageBody() {
	pairChat.send(new Message("&"));
	session.verifySent("<message from='self@domain/res' to='other@domain/other' type='chat'>"
		+ "<body>&amp;</body><thread>theThread</thread></message>");
    }

}
