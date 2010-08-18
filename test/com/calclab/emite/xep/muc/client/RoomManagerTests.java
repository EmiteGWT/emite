package com.calclab.emite.xep.muc.client;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.client.events.ChangedEvent.ChangeTypes;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.xtesting.XmppSessionTester;
import com.calclab.emite.xtesting.handlers.ChatChangedTestHandler;

public class RoomManagerTests {

    private static final XmppURI USER = XmppURI.uri("user@domain/res");
    private XmppSessionTester session;
    private RoomChatManager manager;

    @Before
    public void beforeTests() {
	this.session = new XmppSessionTester(USER);
	this.manager = new RoomChatManager(session);
    }

    @Test
    public void shouldAcceptInvitations() {
	ChatChangedTestHandler handler = new ChatChangedTestHandler();
	manager.addChatChangedHandler(handler);
	RoomInvitation invitation = new RoomInvitation(uri("friend@host/resource"), uri("room@room.service"),
		"theReason");
	manager.acceptRoomInvitation(invitation);
	assertEquals(2, handler.getCalledTimes());
	assertEquals(ChangeTypes.created, handler.getEvent(0).getChangeType());
	assertEquals(ChangeTypes.opened, handler.getEvent(1).getChangeType());
    }
}
