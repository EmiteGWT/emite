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
package com.calclab.emite.core.client.xmpp.session;

import java.util.HashMap;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.suco.client.events.Listener;

/**
 * Handles IQ listeners and generates uniqe ids based on category strings. Used
 * by XmppSession and not intended to be used outside
 * 
 */
class IQManager {
    private int id;
    private final HashMap<String, Listener<IPacket>> listeners;

    public IQManager() {
	id = 0;
	this.listeners = new HashMap<String, Listener<IPacket>>();
    }

    public boolean handle(final IPacket received) {
	final String key = received.getAttribute("id");
	final Listener<IPacket> listener = listeners.remove(key);
	final boolean isHandled = listener != null;
	if (isHandled) {
	    listener.onEvent(received);
	}
	return isHandled;
    }

    public String register(final String category, final Listener<IPacket> listener) {
	id++;
	final String key = category + "_" + id;
	listeners.put(key, listener);
	return key;
    }
}
