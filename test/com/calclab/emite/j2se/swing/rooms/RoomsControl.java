/*
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008-2009 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.calclab.emite.j2se.swing.rooms;

import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.session.Session.State;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.xep.muc.client.RoomManager;
import com.calclab.suco.client.events.Listener;

public class RoomsControl {

    public RoomsControl(final Session session, final RoomsPanel roomsPanel, final RoomManager manager) {
	roomsPanel.setEnabled(false);

	session.onStateChanged(new Listener<State>() {
	    public void onEvent(final State state) {
		final boolean isReady = state == State.ready;
		roomsPanel.setEnabled(isReady);
	    }

	});

	roomsPanel.onOpenRoom(new Listener<String>() {
	    public void onEvent(final String roomURI) {
		manager.open(XmppURI.uri(roomURI));
	    }
	});
    }

}
