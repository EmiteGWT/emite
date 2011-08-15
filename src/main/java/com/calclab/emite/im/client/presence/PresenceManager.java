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

package com.calclab.emite.im.client.presence;

import com.calclab.emite.core.client.xmpp.stanzas.Presence;
import com.calclab.emite.im.client.presence.events.OwnPresenceChangedHandler;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Handles the user's presence state. Provides event to know when it changes.
 * 
 * Its responsable of send the initial presence after session login.
 * 
 * @see http://xmpp.org/rfcs/rfc3921.html#presence
 */
public interface PresenceManager {
	/**
	 * Adds a handler to know when the current user's presence changed
	 * 
	 * @param handler
	 * @return
	 */
	HandlerRegistration addOwnPresenceChangedHandler(OwnPresenceChangedHandler handler);

	/**
	 * Change the current user presence
	 */
	void changeOwnPresence(Presence presence);

	/**
	 * Get the current user's presence
	 * 
	 * @return
	 */
	Presence getOwnPresence();

}
