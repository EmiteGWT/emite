package com.calclab.emite.im.client.chat;

import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.xtesting.XmppSessionTester;

/**
 * Test the functionality found in AbstractChat
 * 
 * @author dani
 * 
 */
public class AbstractChatTests {

    private XmppSessionTester session;

    @Before
    public void beforeTests() {
	session = new XmppSessionTester();
    }

    @Test
    public void shouldHaveProperties() {
	ChatProperties properties = new ChatProperties(XmppURI.uri("some@domain"));
	PairChat chat = new PairChat(session, properties);
	assertSame(properties, chat.getProperties());
    }

}
