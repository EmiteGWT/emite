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

package com.calclab.emite.example.pingpong.client.events;

import com.calclab.emite.core.client.events.ChangedEvent.ChangeTypes;
import com.calclab.emite.core.client.events.PresenceEvent;
import com.calclab.emite.core.client.events.PresenceHandler;
import com.calclab.emite.im.client.chat.events.ChatChangedEvent;
import com.calclab.emite.im.client.chat.events.ChatChangedHandler;
import com.calclab.emite.xep.muc.client.Occupant;
import com.calclab.emite.xep.muc.client.Room;
import com.calclab.emite.xep.muc.client.RoomManager;
import com.calclab.emite.xep.muc.client.events.OccupantChangedEvent;
import com.calclab.emite.xep.muc.client.events.OccupantChangedHandler;
import com.calclab.emite.example.pingpong.client.PingPongDisplay;
import com.calclab.emite.example.pingpong.client.PingPongDisplay.Style;
import com.google.inject.Inject;

public class RoomManagerEventsSupervisor {

	@Inject
	public RoomManagerEventsSupervisor(final RoomManager roomManager, final PingPongDisplay display) {
		roomManager.addChatChangedHandler(new ChatChangedHandler() {
			@Override
			public void onChatChanged(final ChatChangedEvent event) {
				if (event.is(ChangeTypes.created)) {
					trackRoom((Room) event.getChat(), display);
				}
			}
		});
	}

	protected void trackRoom(final Room room, final PingPongDisplay display) {
		room.addOccupantChangedHandler(new OccupantChangedHandler() {
			@Override
			public void onOccupantChanged(final OccupantChangedEvent event) {
				display.print("ROOM OCCUPANT " + event.getOccupant().getNick() + " changed: " + event.getChangeType(), Style.event);
				String occupants = "";
				for (final Occupant occupant : room.getOccupants()) {
					occupants += occupant.getOccupantUri().getResource() + " ";
				}
				display.print("ROOM OCCUPANTS (" + room.getOccupantsCount() + "): " + occupants, Style.event);
			}
		});

		room.addPresenceReceivedHandler(new PresenceHandler() {
			@Override
			public void onPresence(final PresenceEvent event) {
				display.print("ROOM PRESENCE : " + event.getPresence(), Style.event);
			}
		});
	}

}
