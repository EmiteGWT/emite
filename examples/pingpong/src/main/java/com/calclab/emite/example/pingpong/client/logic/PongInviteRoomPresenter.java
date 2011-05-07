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

package com.calclab.emite.example.pingpong.client.logic;

import com.calclab.emite.core.client.events.StateChangedEvent;
import com.calclab.emite.core.client.events.StateChangedHandler;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.im.client.chat.Chat;
import com.calclab.emite.im.client.chat.ChatStates;
import com.calclab.emite.im.client.chat.events.ChatChangedEvent;
import com.calclab.emite.im.client.chat.events.ChatChangedHandler;
import com.calclab.emite.xep.muc.client.Room;
import com.calclab.emite.xep.muc.client.RoomInvitation;
import com.calclab.emite.xep.muc.client.RoomManager;
import com.calclab.emite.xep.muc.client.events.RoomInvitationEvent;
import com.calclab.emite.xep.muc.client.events.RoomInvitationHandler;
import com.calclab.emite.xep.muc.client.subject.RoomSubject;
import com.calclab.emite.xep.muc.client.subject.RoomSubjectChangedEvent;
import com.calclab.emite.xep.muc.client.subject.RoomSubjectChangedHandler;
import com.calclab.emite.example.pingpong.client.PingPongDisplay;
import com.calclab.emite.example.pingpong.client.PingPongDisplay.Style;
import com.calclab.emite.example.pingpong.client.StartablePresenter;
import com.calclab.emite.example.pingpong.client.events.ChatManagerEventsSupervisor;
import com.calclab.emite.example.pingpong.client.events.RoomManagerEventsSupervisor;
import com.google.gwt.user.client.Timer;
import com.google.inject.Inject;

public class PongInviteRoomPresenter implements StartablePresenter {

	private final PingPongDisplay display;
	private int time;
	private int pongs;
	private final RoomManager roomManager;

	@Inject
	public PongInviteRoomPresenter(final RoomManager roomManager, final PingPongDisplay display) {
		this.roomManager = roomManager;
		this.display = display;
		time = 5000;
		pongs = 0;
	}

	@Override
	public void start() {
		display.printHeader("This is pong invite room example", Style.title);
		display.print("You need to open the ping invite room example page", Style.important);

		new ChatManagerEventsSupervisor(roomManager, display);
		new RoomManagerEventsSupervisor(roomManager, display);

		// Accept the room invitations we receive
		roomManager.addRoomInvitationReceivedHandler(new RoomInvitationHandler() {
			@Override
			public void onRoomInvitation(final RoomInvitationEvent event) {
				final RoomInvitation invitation = event.getRoomInvitation();
				display.print("Room invitation received: " + invitation.getReason() + " - " + invitation.getInvitor() + " to " + invitation.getRoomURI(),
						Style.important);
				display.print("We accept the invitation", Style.important);
				roomManager.acceptRoomInvitation(invitation);
			}
		});

		// When a room is opened (by the acceptRoomInvitation method) we stay
		// for a while and then go out
		roomManager.addChatChangedHandler(new ChatChangedHandler() {
			@Override
			public void onChatChanged(final ChatChangedEvent event) {
				if (event.isCreated()) {
					manageNewRoom(roomManager, (Room) event.getChat());
				}
			}
		});

	}

	private void closeRoom(final RoomManager manager, final Chat room) {
		new Timer() {
			@Override
			public void run() {
				display.print("We close the room: " + room.getURI(), Style.important);
				time += 2000;
				manager.close(room);
			}

		}.schedule(time);
	}

	private void manageNewRoom(final RoomManager manager, final Room room) {
		display.print("Room created: " + room.getURI(), Style.info);
		room.addChatStateChangedHandler(true, new StateChangedHandler() {
			@Override
			public void onStateChanged(final StateChangedEvent event) {
				if (event.is(ChatStates.ready)) {
					display.print("We entered the room: " + room.getURI(), Style.info);
					pongs++;
					room.send(new Message("Pong " + pongs));
					closeRoom(manager, room);
				}
			}
		});

		RoomSubject.addRoomSubjectChangedHandler(room, new RoomSubjectChangedHandler() {
			@Override
			public void onSubjectChanged(final RoomSubjectChangedEvent event) {
				display.print("Subject changed: " + event.getSubject() + "(" + event.getOccupantUri() + ")", Style.important);
			}
		});

	}

}
