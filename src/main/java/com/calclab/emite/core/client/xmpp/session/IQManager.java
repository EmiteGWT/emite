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

package com.calclab.emite.core.client.xmpp.session;

import java.util.HashMap;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.xmpp.stanzas.IQ;

/**
 * Handles IQ listeners and generates uniqe ids based on category strings. Used
 * by XmppSession and not intended to be used outside
 * 
 */
class IQManager {
	private int id;
	private final HashMap<String, IQResponseHandler> handlers;

	public IQManager() {
		id = 0;
		handlers = new HashMap<String, IQResponseHandler>();
	}

	public boolean handle(final IPacket received) {
		final String key = received.getAttribute("id");
		final IQResponseHandler handler = handlers.remove(key);
		if (handler == null)
			return false;

		handler.onIQ(new IQ(received));
		return true;
	}

	public String register(final String category, final IQResponseHandler handler) {
		id++;
		final String key = category + "_" + id;
		handlers.put(key, handler);
		return key;
	}

}
