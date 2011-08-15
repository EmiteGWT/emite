/*
 * ((e)) emite: A pure Google Web Toolkit XMPP library
 * Copyright (c) 2008-2011 The Emite development team
 * 
 * This file is part of Emite.
 *
 * Emite is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * Emite is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with Emite.  If not, see <http://www.gnu.org/licenses/>.
 */

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

	public static void addRoomSubjectChangedHandler(final Room room, final RoomSubjectChangedHandler handler) {
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
	public static boolean requestSubjectChange(final Room room, final String subjectText) {
		final XmppSession session = room.getSession();
		final BasicStanza message = new BasicStanza("message", null);
		message.setFrom(session.getCurrentUserURI());
		message.setTo(room.getURI().getJID());
		message.setType(Message.Type.groupchat.toString());
		final IPacket subject = message.addChild("subject", null);
		subject.setText(subjectText);
		session.send(message);
		return true;
	}

	private static boolean hasRoom(final Room room) {
		if (RoomSubject.rooms == null) {
			RoomSubject.rooms = new ArrayList<Room>();
		}
		return rooms.contains(room);
	}

	private static void trackSubjectChangeMessages(final Room room) {
		RoomSubject.rooms.add(room);
		room.addMessageReceivedHandler(new MessageHandler() {
			@Override
			public void onMessage(final MessageEvent event) {
				final Message message = event.getMessage();
				if (message.getSubject() != null) {
					room.getChatEventBus().fireEvent(new RoomSubjectChangedEvent(message.getFrom(), message.getSubject()));
				}
			}
		});
	}
}
