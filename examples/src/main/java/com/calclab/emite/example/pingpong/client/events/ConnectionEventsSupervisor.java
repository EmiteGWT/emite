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

import com.calclab.emite.core.client.conn.ConnectionStateChangedEvent;
import com.calclab.emite.core.client.conn.ConnectionStateChangedHandler;
import com.calclab.emite.core.client.conn.StanzaEvent;
import com.calclab.emite.core.client.conn.StanzaHandler;
import com.calclab.emite.core.client.conn.XmppConnection;
import com.calclab.emite.example.pingpong.client.PingPongDisplay;
import com.calclab.emite.example.pingpong.client.PingPongDisplay.Style;
import com.google.inject.Inject;

public class ConnectionEventsSupervisor {

	@Inject
	public ConnectionEventsSupervisor(final XmppConnection connection, final PingPongDisplay display) {
		connection.addStanzaReceivedHandler(new StanzaHandler() {
			@Override
			public void onStanza(final StanzaEvent event) {
				display.print("IN: " + event.getStanza(), Style.stanzaReceived);
			}
		});

		connection.addStanzaSentHandler(new StanzaHandler() {
			@Override
			public void onStanza(final StanzaEvent event) {
				display.print("OUT: " + event.getStanza(), Style.stanzaSent);
			}
		});

		connection.addConnectionStateChangedHandler(new ConnectionStateChangedHandler() {
			@Override
			public void onStateChanged(final ConnectionStateChangedEvent event) {
				display.print("Connection state: " + event.getState(), Style.info);
			}
		});
	}

}
