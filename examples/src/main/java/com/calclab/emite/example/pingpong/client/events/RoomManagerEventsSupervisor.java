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

import com.calclab.emite.xep.muc.client.Occupant;
import com.calclab.emite.xep.muc.client.OccupantChangedEvent;
import com.calclab.emite.xep.muc.client.RoomChat;
import com.calclab.emite.xep.muc.client.RoomChatChangedEvent;
import com.calclab.emite.xep.muc.client.RoomChatManager;
import com.calclab.emite.core.client.events.ChangedEvent.ChangeType;
import com.calclab.emite.core.client.events.PresenceReceivedEvent;
import com.calclab.emite.example.pingpong.client.PingPongDisplay;
import com.calclab.emite.example.pingpong.client.PingPongDisplay.Style;
import com.google.inject.Inject;

public class RoomManagerEventsSupervisor implements RoomChatChangedEvent.Handler {

	private final PingPongDisplay display;
	
	@Inject
	public RoomManagerEventsSupervisor(final RoomChatManager roomManager, final PingPongDisplay display) {
		this.display = display;
		
		roomManager.addRoomChatChangedHandler(this);
	}
	
	@Override
	public void onRoomChatChanged(final RoomChatChangedEvent event) {
		final RoomChat room = event.getChat();
		
		if (event.is(ChangeType.created)) {
			room.addOccupantChangedHandler(new OccupantChangedEvent.Handler() {
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

			room.addPresenceReceivedHandler(new PresenceReceivedEvent.Handler() {
				@Override
				public void onPresenceReceived(final PresenceReceivedEvent event) {
					display.print("ROOM PRESENCE : " + event.getPresence(), Style.event);
				}
			});
		}
	}

}
