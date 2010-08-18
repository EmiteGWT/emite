package com.calclab.emite.xep.muc.client;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.client.events.MessageReceivedEvent;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.core.client.xmpp.stanzas.Presence.Show;
import com.calclab.emite.im.client.chat.AbstractChat;
import com.calclab.emite.im.client.chat.AbstractChatTest;
import com.calclab.emite.im.client.chat.Chat;
import com.calclab.emite.im.client.chat.ChatProperties;
import com.calclab.emite.im.client.chat.Chat.State;
import com.calclab.suco.testing.events.MockedListener;
import com.calclab.suco.testing.events.MockedListener2;

public class RoomTest extends AbstractChatTest {
    private RoomChat room;
    private XmppURI userURI;
    private XmppURI roomURI;

    @Before
    public void beforeTests() {
	userURI = uri("user@domain/res");
	roomURI = uri("room@domain/nick");
	session.setLoggedIn(userURI);
	RoomChatManager manager = new RoomChatManager(xmppSession);
	final ChatProperties properties = new ChatProperties(roomURI, userURI, null);
	room = (RoomChat) manager.openChat(properties, true);
    }

    @Override
    public AbstractChat getChat() {
	return room;
    }

    @Test
    public void shouldAddOccupantAndFireListeners() {
	final MockedListener<Occupant> listener = new MockedListener<Occupant>();
	room.onOccupantAdded(listener);
	final XmppURI uri = uri("room@domain/name");
	final Occupant occupant = room.setOccupantPresence(uri, "aff", "role", Show.unknown, null);
	assertTrue(listener.isCalledOnce());
	final Occupant result = room.getOccupantByURI(uri);
	assertEquals(occupant, result);
    }

    @Test
    public void shouldChangeSubject() {
	room.requestSubjectChange("Some subject");
	xmppSession.verifySent("<message type=\"groupchat\" from=\"" + userURI + "\" to=\"" + room.getURI().getJID()
		+ "\"><subject>Some subject</subject></message></body>");
    }

    @Test
    public void shouldExitAndLockTheRoomWhenLoggedOut() {
	receiveInstantRoomCreation(roomURI);
	xmppSession.logout();
	assertEquals(Chat.State.locked, room.getState());
	xmppSession.verifySent("<presence to='room@domain/nick' type='unavailable'/>");
    }

    @Test
    public void shouldFireListenersWhenMessage() {
	final MockedListener<Message> listener = new MockedListener<Message>();
	room.onMessageReceived(listener);
	final Message message = new Message("message", uri("room@domain"), uri("someone@domain/res"));
	room.getChatEventBus().fireEvent(new MessageReceivedEvent(message));
	assertTrue(listener.isCalledWithEquals(message));
    }

    @Test
    public void shouldFireListenersWhenSubjectChange() {
	final MockedListener2<Occupant, String> subjectListener = new MockedListener2<Occupant, String>();
	room.onSubjectChanged(subjectListener);

	final XmppURI occupantURI = uri("someone@domain/res");
	room.getChatEventBus().fireEvent(
		new MessageReceivedEvent(new Message(null, uri("room@domain"), occupantURI).Subject("the subject")));
	assertEquals(1, subjectListener.getCalledTimes());
	final Occupant occupant = room.getOccupantByURI(occupantURI);
	assertTrue(subjectListener.isCalledWithSame(occupant, "the subject"));
    }

    @Test
    public void shouldRemoveOccupant() {
	final MockedListener<Occupant> occupantRemoved = new MockedListener<Occupant>();
	room.onOccupantRemoved(occupantRemoved);
	final XmppURI uri = uri("room@domain/name");
	room.setOccupantPresence(uri, "owner", "participant", Show.notSpecified, null);
	assertEquals(1, room.getOccupantsCount());
	room.removeOccupant(uri);
	assertEquals(0, room.getOccupantsCount());
	assertEquals(1, occupantRemoved.getCalledTimes());
	assertNull(room.getOccupantByURI(uri));
    }

    @Test
    public void shouldSendRoomInvitation() {
	room.sendInvitationTo(uri("otherUser@domain/resource"), "this is the reason");
	xmppSession.verifySent("<message from='" + userURI + "' to='" + roomURI.getJID()
		+ "'><x xmlns='http://jabber.org/protocol/muc#user'>"
		+ "<invite to='otheruser@domain/resource'><reason>this is the reason</reason></invite></x></message>");
    }

    @Test
    public void shouldSendRoomPresenceWhenCreated() {
	xmppSession
		.verifySent("<presence to='room@domain/nick'><x xmlns='http://jabber.org/protocol/muc' /></presence>");
    }

    @Test
    public void shouldUnlockWhenInstantRoomIsCreated() {
	final MockedListener<State> stateChanged = new MockedListener<Chat.State>();
	room.onStateChanged(stateChanged);
	assertEquals(State.locked, room.getState());
	receiveInstantRoomCreation(roomURI);
	assertTrue(stateChanged.isCalledOnce());
    }

    @Test
    public void shouldUpdateOccupantAndFireListeners() {
	final MockedListener<Occupant> listener = new MockedListener<Occupant>();
	room.onOccupantModified(listener);
	final XmppURI uri = uri("room@domain/name");
	final Occupant occupant = room.setOccupantPresence(uri, "owner", "participant", Show.notSpecified, null);
	final Occupant occupant2 = room.setOccupantPresence(uri, "admin", "moderator", Show.notSpecified, null);
	assertEquals(1, listener.getCalledTimes());
	assertSame(occupant, occupant2);
    }

    private void receiveInstantRoomCreation(final XmppURI room) {
	xmppSession.receives("<presence to='user@domain/res' from='" + room + "'>"
		+ "<x xmlns='http://jabber.org/protocol/muc#user'>"
		+ "<item affiliation='owner' role='moderator'/><status code='201'/></x></presence>");
	xmppSession.verifyIQSent("<iq to='" + room.getJID() + "' type='set'>"
		+ "<query xmlns='http://jabber.org/protocol/muc#owner'>"
		+ "<x xmlns='jabber:x:data' type='submit'/></query></iq>");
	xmppSession.answerSuccess();
    }

}
