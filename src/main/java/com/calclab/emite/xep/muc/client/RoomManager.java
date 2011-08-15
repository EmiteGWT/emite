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

package com.calclab.emite.xep.muc.client;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.emite.xep.muc.client.events.RoomInvitationHandler;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * RoomManager: room related methods
 * 
 * @see ChatManager
 */
public interface RoomManager extends ChatManager {
	/**
	 * Accepts a room invitation event
	 * 
	 * @param invitation
	 *            the invitation event to be accepted
	 */
	public Room acceptRoomInvitation(RoomInvitation invitation);

	/**
	 * Add a handler to know when a room invitation has arrived
	 * 
	 * @param handler
	 * @return
	 */
	public HandlerRegistration addRoomInvitationReceivedHandler(RoomInvitationHandler handler);

	/**
	 * Obtain the default history options applied to all new rooms
	 * 
	 * @return
	 */
	public HistoryOptions getDefaultHistoryOptions();

	public Room open(final XmppURI uri, HistoryOptions historyOptions);

	public void setDefaultHistoryOptions(HistoryOptions historyOptions);
}
