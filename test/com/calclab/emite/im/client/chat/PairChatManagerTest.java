package com.calclab.emite.im.client.chat;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.im.client.chat.Chat.State;
import com.calclab.emite.xep.chatstate.client.ChatStateManager;
import com.calclab.suco.testing.events.MockedListener;

public class PairChatManagerTest extends AbstractChatManagerTest {

    private MockedListener<Chat> addOnChatCreatedListener() {
	final MockedListener<Chat> listener = new MockedListener<Chat>();
	manager.onChatCreated(listener);
	return listener;
    }

    @Test
    public void chatStateDontFireOnChatCreatedButMustAfterOpenChat() {
	final Message message = new Message(OTHER, MYSELF, null);
	message.addChild("gone", ChatStateManager.XMLNS);

	final MockedListener<Chat> listener = addOnChatCreatedListener();
	session.receives(message);
	assertTrue(listener.isNotCalled());
	manager.open(OTHER);
	assertTrue(listener.isCalled());
    }

    @Override
    protected PairChatManager createChatManager() {
	final PairChatManager chatManagerDefault = new PairChatManager(session);
	return chatManagerDefault;
    }

    @Test
    public void managerShouldCreateOneChatForSameResource() {
	final MockedListener<Chat> listener = addOnChatCreatedListener();
	session.receives(new Message(uri("source@domain/resource1"), MYSELF, "message 1"));
	session.receives(new Message(uri("source@domain/resource1"), MYSELF, "message 2"));
	assertEquals(1, listener.getCalledTimes());
    }

    @Test
    public void oneToOneChatsAreAlwaysReadyWhenCreated() {
	final Chat chat = manager.open(uri("other@domain/resource"));
	assertSame(Chat.State.ready, chat.getState());
    }

    @Test
    public void roomInvitationsShouldDontFireOnChatCreated() {
	final MockedListener<Chat> listener = addOnChatCreatedListener();
	session.receives("<message to='" + MYSELF
		+ "' from='someroom@domain'><x xmlns='http://jabber.org/protocol/muc#user'>" + "<invite from='" + OTHER
		+ "'><reason>Join to our conversation</reason></invite>"
		+ "</x><x jid='someroom@domain' xmlns='jabber:x:conference' /></message>");
	assertTrue(listener.isNotCalled());
    }

    @Test
    public void roomInvitationsShouldDontFireOnChatCreatedButMustAfterOpenChat() {
	final MockedListener<Chat> listener = addOnChatCreatedListener();
	session.receives("<message to='" + MYSELF
		+ "' from='someroom@domain'><x xmlns='http://jabber.org/protocol/muc#user'>" + "<invite from='" + OTHER
		+ "'><reason>Join to our conversation</reason></invite>"
		+ "</x><x jid='someroom@domain' xmlns='jabber:x:conference' /></message>");
	assertTrue(listener.isNotCalled());
	manager.open(OTHER);
	assertTrue(listener.isCalled());
    }

    @Test
    public void shouldBeInitiatedByOtherIfMessageArrives() {
	session.receives("<message to='" + MYSELF + "' from='someone@domain'><body>the body</body></message>");
	final Chat chat = manager.open(uri("someone@domain"));
	assertFalse(chat.isInitiatedByMe());
    }

    @Test
    public void shouldBlockChatWhenClosingIt() {
	final Chat chat = manager.open(uri("other@domain/resource"));
	assertSame(Chat.State.ready, chat.getState());
	manager.close(chat);
	assertSame(Chat.State.locked, chat.getState());
    }

    @Test
    public void shouldCloseChatWhenLoggedOut() {
	final Chat chat = manager.open(uri("name@domain/resouce"));
	final MockedListener<State> listener = new MockedListener<State>();
	chat.onStateChanged(listener);
	session.logout();
	assertTrue(listener.isCalledWithEquals(State.locked));
    }

    @Test
    public void shouldEventIncommingMessages() {
	final Chat chat = manager.open(uri("someone@domain"));
	final MockedListener<Message> listener = new MockedListener<Message>();
	chat.onMessageReceived(listener);
	session.receives("<message type='chat' id='purplee8b92642' to='user@domain' "
		+ "from='someone@domain'><x xmlns='jabber:x:event'/><active"
		+ "xmlns='http://jabber.org/protocol/chatstates'/></message>");
	assertTrue(listener.isCalledOnce());
    }

    @Test
    public void shouldOpenDifferentsChatsForDifferentDomains() {
	final Chat chatCom = manager.open(uri("COM@domain.com"));
	final MockedListener<Message> listenerCom = new MockedListener<Message>();
	chatCom.onMessageReceived(listenerCom);
	assertTrue("com listener empty", listenerCom.isCalled(0));

	final Chat chatOrg = manager.open(uri("ORG@domain.org"));
	final MockedListener<Message> listenerOrg = new MockedListener<Message>();
	chatOrg.onMessageReceived(listenerOrg);
	assertTrue("org listener empty", listenerOrg.isCalled(0));

	session.receives(new Message(uri("COM@domain.com"), MYSELF, "message com 2"));
	assertTrue("com has one message", listenerCom.isCalled(1));
	assertTrue("org has no message", listenerOrg.isCalled(0));

    }

    @Test
    public void shouldReuseChatIfNotResouceSpecified() {
	final MockedListener<Chat> listener = addOnChatCreatedListener();
	session.receives(new Message(uri("source@domain"), MYSELF, "message 1"));
	session.receives(new Message(uri("source@domain/resource1"), MYSELF, "message 2"));
	assertTrue(listener.isCalled(1));
    }

    @Test
    public void shouldReuseChatWhenAnsweringWithDifferentResources() {
	final MockedListener<Chat> listener = addOnChatCreatedListener();
	final Chat chat = manager.open(uri("someone@domain"));
	assertTrue(listener.isCalledOnce());
	assertTrue(listener.isCalledWithSame(chat));
	session.receives(new Message(uri("someone@domain/resource"), MYSELF, "answer"));
	assertTrue(listener.isCalled(1));
    }
}
