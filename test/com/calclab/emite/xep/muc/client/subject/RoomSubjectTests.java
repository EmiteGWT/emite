package com.calclab.emite.xep.muc.client.subject;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.Chat.ChatStates;
import com.calclab.emite.xep.muc.client.Room;
import com.calclab.emite.xep.muc.client.RoomChatManager;
import com.calclab.emite.xtesting.XmppSessionTester;
import com.calclab.emite.xtesting.handlers.RoomSubjectChangedTestHandler;

/**
 * Test RoomSubject class
 * 
 */
public class RoomSubjectTests {

    private Room room;
    private XmppURI roomUri;
    private XmppSessionTester session;
    private XmppURI userUri;
    private XmppURI occupantUri;

    @Before
    public void beforeTests() {
	userUri = XmppURI.uri("user@domain/resource");
	roomUri = XmppURI.uri("room@conference.domain");
	occupantUri = XmppURI.uri("room@conference.domain/user");
	session = new XmppSessionTester(userUri);
	final RoomChatManager manager = new RoomChatManager(session);
	room = (Room) manager.open(roomUri);
	room.getProperties().setState(ChatStates.ready);
    }

    @Test
    public void shouldChangeSubject() {
	RoomSubject.requestSubjectChange(room, "Some subject");
	session.verifySent("<message type='groupchat' from='" + userUri + "' to='" + room.getURI().getJID()
		+ "' ><subject>Some subject</subject></message></body>");
    }

    @Test
    public void shouldHandleRoomSubjectChangeEvents() {
	RoomSubjectChangedTestHandler handler = new RoomSubjectChangedTestHandler();
	RoomSubject.addRoomSubjectChangedHandler(room, handler);
	room.receive(new Message(null, roomUri, occupantUri).Subject("The subject"));
	assertEquals(1, handler.getCalledTimes());
	assertEquals("The subject", handler.getLastEvent().getSubject());
    }
}
