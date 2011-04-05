package com.calclab.emite.xep.muc.client.subject;

import java.util.ArrayList;

import com.calclab.emite.core.client.events.MessageEvent;
import com.calclab.emite.core.client.events.MessageHandler;
import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.calclab.emite.core.client.xmpp.stanzas.BasicStanza;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.xep.muc.client.Room;

/**
 * A class to perform room subject changes and track those changes
 */
public class RoomSubject {

    private static ArrayList<Room> rooms;

    public static void addRoomSubjectChangedHandler(Room room, RoomSubjectChangedHandler handler) {
	if (!hasRoom(room)) {
	    trackSubjectChangeMessages(room);
	}
	RoomSubjectChangedEvent.bind(room.getChatEventBus(), handler);
    }

    /**
     * Request a subject change on the given room
     * 
     * @param room
     * @param subjectText
     * @return true if the subject request has been sent
     */
    // TODO: check occupants affiliation to see if the user can do that!!
    public static boolean requestSubjectChange(Room room, final String subjectText) {
	XmppSession session = room.getSession();
	final BasicStanza message = new BasicStanza("message", null);
	message.setFrom(session.getCurrentUserURI());
	message.setTo(room.getURI().getJID());
	message.setType(Message.Type.groupchat.toString());
	final IPacket subject = message.addChild("subject", null);
	subject.setText(subjectText);
	session.send(message);
	return true;
    }

    private static boolean hasRoom(Room room) {
	if (RoomSubject.rooms == null) {
	    RoomSubject.rooms = new ArrayList<Room>();
	}
	return rooms.contains(room);
    }

    private static void trackSubjectChangeMessages(final Room room) {
	RoomSubject.rooms.add(room);
	room.addMessageReceivedHandler(new MessageHandler() {
	    @Override
	    public void onMessage(MessageEvent event) {
		Message message = event.getMessage();
		if (message.getSubject() != null) {
		    room.getChatEventBus().fireEvent(
			    new RoomSubjectChangedEvent(message.getFrom(), message.getSubject()));
		}
	    }
	});
    }
}
