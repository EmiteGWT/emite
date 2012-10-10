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

package com.calclab.emite.xep.muc;

import java.util.Collection;

import com.calclab.emite.core.XmppURI;
import com.calclab.emite.xep.muc.events.RoomChatChangedEvent;
import com.calclab.emite.xep.muc.events.RoomInvitationReceivedEvent;
import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * RoomManager: room related methods
 * 
 * @see ChatManager
 */
public interface RoomChatManager {

	/**
	 * Add a handler to track chat changes. The following changes can occur from
	 * a default chat manager: created, opened, closed
	 * 
	 * @param handler
	 */
	HandlerRegistration addRoomChatChangedHandler(RoomChatChangedEvent.Handler handler);

	/**
	 * Add a handler to know when a room invitation has arrived
	 * 
	 * @param handler
	 * @return
	 */
	HandlerRegistration addRoomInvitationReceivedHandler(RoomInvitationReceivedEvent.Handler handler);
	
	/**
	 * Accepts a room invitation event
	 * 
	 * @param invitation
	 *            the invitation event to be accepted
	 */
	RoomChat acceptRoomInvitation(RoomInvitation invitation, HistoryOptions historyOptions);
	
	/**
	 * The same as getChat, but it fire ChatChanged(opened) event if the chat is
	 * found or created
	 * 
	 * @param uri
	 * @return
	 */
	RoomChat openRoom(XmppURI uri, HistoryOptions historyOptions);
	
	/**
	 * Same as getChat(new ChatProperties(uri), false);
	 * 
	 * Here for compatibility reasons.
	 * 
	 * @param uri
	 * @return
	 */
	RoomChat getRoom(XmppURI uri);
	
	Collection<RoomChat> getRooms();
	
}
