package com.calclab.emite.xep.chatstate.client;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.Chat;
import com.calclab.emite.im.client.chat.PairChatManager;
import com.calclab.emite.xep.chatstate.client.ChatStateManager.ChatState;
import com.calclab.emite.xtesting.XmppSessionTester;
import com.calclab.suco.testing.events.MockedListener;

public class ChatStateManagerTest {
    private static final XmppURI MYSELF = uri("self@domain/res");
    private static final XmppURI OTHER = uri("other@domain/other");

    private PairChatManager chatManager;
    private MockedListener<ChatState> stateListener;
    private Chat chat;
    private ChatStateManager chatStateManager;
    private XmppSessionTester session;

    @Before
    public void beforeTests() {
	session = new XmppSessionTester();
	chatManager = new PairChatManager(session);
	session.setLoggedIn(MYSELF);
	final StateManager stateManager = new StateManager(chatManager);
	chat = chatManager.open(OTHER);
	chatStateManager = stateManager.getChatState(chat);
	stateListener = new MockedListener<ChatState>();
	chatStateManager.onChatStateChanged(stateListener);
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
		+ "<message from='self@domain/res' to='other@domain/other' type='chat'>"
		+ "<pause xmlns='http://jabber.org/protocol/chatstates'/></message>");
    }

    @Test
    public void shouldStartStateAfterNegotiation() {
	chat.send(new Message("test message"));
	session.receives("<message from='other@domain/other' to='self@domain/res' type='chat'>"
		+ "<active xmlns='http://jabber.org/protocol/chatstates'/></message>");
	final Message message = new Message(MYSELF, OTHER, "test message");
	message.addChild(ChatStateManager.ChatState.active.toString(), ChatStateManager.XMLNS);
	chatStateManager.setOwnState(ChatStateManager.ChatState.composing);
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
	chatStateManager.setOwnState(ChatStateManager.ChatState.composing);
	chatStateManager.setOwnState(ChatStateManager.ChatState.pause);
	session.verifySent("<message><active /></message>");
	session.verifyNotSent("<message><composing /></message>");
	session.verifyNotSent("<message><pause /></message>");
	session.verifyNotSent("<message><gone /></message>");
    }

}
