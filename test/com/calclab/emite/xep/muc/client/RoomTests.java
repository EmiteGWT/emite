package com.calclab.emite.xep.muc.client;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.Chat.ChatStates;
import com.calclab.emite.xtesting.XmppSessionTester;
import com.calclab.emite.xtesting.handlers.MessageTestHandler;

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

    @Test
    public void shouldInterceptOutcomingMessages() {
	// a little hack
	room.getProperties().setState(ChatStates.ready);
	MessageTestHandler handler = new MessageTestHandler();
	room.addBeforeSendMessageHandler(handler);
	room.send(new Message("body"));
	assertTrue(handler.isCalledOnce());
	assertEquals("body", handler.getLastMessage().getBody());
    }
}
