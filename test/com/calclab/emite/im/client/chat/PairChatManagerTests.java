package com.calclab.emite.im.client.chat;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.xtesting.XmppSessionTester;

public class PairChatManagerTests {

    private static final XmppURI ME = XmppURI.uri("me@localhost");
    private static final XmppURI OTHER = XmppURI.uri("other@localhost");

    private XmppSessionTester session;
    private PairChatManager manager;

    @Before
    public void beforeTests() {
	session = new XmppSessionTester();
	manager = new PairChatManager(session);
    }

    @Test
    public void shouldCloseChatsWhenLogout() {

    }

    @Test
    public void shouldOpenChatIfLogin() {
	session.login(ME, "");
	manager.getChat(OTHER);
    }

    @Test
    public void shouldOpenChatWhenLogin() {

    }

}
