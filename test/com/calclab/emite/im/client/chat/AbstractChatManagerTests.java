package com.calclab.emite.im.client.chat;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.client.xmpp.session.XmppSession.SessionStates;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.xtesting.XmppSessionTester;

public abstract class AbstractChatManagerTests {
    protected static final XmppURI ME = XmppURI.uri("me@localhost");
    protected static final XmppURI OTHER = XmppURI.uri("other@localhost");

    protected XmppSessionTester session;
    protected PairChatManager manager;

    @Before
    public void beforeTests() {
	session = new XmppSessionTester();
	manager = createChatManager(session);
    }

    @Test
    public void shouldCloseChatsWhenDisconnected() {
	session.setLoggedIn(ME);
	Chat chat = manager.open(OTHER);
	assertEquals(ChatStates.ready, chat.getChatState());
	session.setSessionState(SessionStates.disconnected);
	assertEquals(ChatStates.locked, chat.getChatState());
    }

    @Test
    public void shouldCloseChatsWhenLogout() {
	session.setLoggedIn(ME);
	Chat chat = manager.open(OTHER);
	assertEquals(ChatStates.ready, chat.getChatState());
	session.logout();
	assertEquals(ChatStates.locked, chat.getChatState());

    }

    protected abstract PairChatManager createChatManager(XmppSessionTester session2);
}
