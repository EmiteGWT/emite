package com.calclab.emite.xep.muc.client;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.xtesting.XmppSessionTester;

public class RoomTests {

    private XmppSessionTester session;
    private RoomChatManager manager;
    private Room room;
    private XmppURI userURI;
    private XmppURI roomURI;

    @Before
    public void beforeTest() {
	userURI = uri("user@domain/res");
	roomURI = uri("room@domain/user");
	this.session = new XmppSessionTester(userURI);
	this.manager = new RoomChatManager(session);
	this.room = (Room) manager.open(roomURI);
    }

    @Test
    public void shouldIdentifyUserAndAdminMessages() {
	Message adminMessage = new Message("admin");
	adminMessage.setFrom(roomURI.getJID());
	assertFalse(room.isUserMessage(adminMessage));
	Message userMessage = new Message("user");
	userMessage.setFrom(XmppURI.uri("room@domain/someone"));
	assertTrue(room.isUserMessage(userMessage));
    }
}
