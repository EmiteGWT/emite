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

import com.calclab.emite.core.client.events.MessageEvent;
import com.calclab.emite.core.client.events.MessageHandler;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.xep.muc.client.Room;
import com.calclab.emite.xep.muc.client.RoomManager;
import com.calclab.emite.example.pingpong.client.PingPongDisplay;
import com.calclab.emite.example.pingpong.client.PingPongDisplay.Style;
import com.calclab.emite.example.pingpong.client.StartablePresenter;
import com.calclab.emite.example.pingpong.client.events.ChatManagerEventsSupervisor;
import com.calclab.emite.example.pingpong.client.events.RoomManagerEventsSupervisor;
import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * Receives pings from a room and send pongs back.
 * 
 */
public class PongRoomPresenter implements StartablePresenter {

	private final XmppURI roomUri;
	private final PingPongDisplay display;
	private int pongs;
	private final RoomManager roomManager;

	@Inject
	public PongRoomPresenter(final RoomManager roomManager, @Named("room") final XmppURI roomUri, final PingPongDisplay display) {
		this.roomManager = roomManager;
		this.roomUri = roomUri;
		this.display = display;
		pongs = 0;
	}

	@Override
	public void start() {
		display.printHeader("This is pong room example", Style.title);
		display.printHeader("Pong from: " + roomUri, Style.info);
		display.printHeader("You need to open the ping room example page in order to run the example", Style.important);

		new ChatManagerEventsSupervisor(roomManager, display);
		new RoomManagerEventsSupervisor(roomManager, display);

		// Because is a RoomManager, we know this MUST be a room
		final Room room = (Room) roomManager.open(roomUri);
		room.addMessageReceivedHandler(new MessageHandler() {
			@Override
			public void onMessage(final MessageEvent event) {
				final Message message = event.getMessage();
				display.print(("RECEIVED: " + message.getBody()), Style.received);
				pongs++;
				if (room.isUserMessage(message) && !room.isComingFromMe(message)) {
					final String body = "Pong " + pongs + " [" + System.currentTimeMillis() + "]";
					room.send(new Message(body));
					display.print("SENT: " + body, Style.sent);
				}
			}
		});
	}

}
