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

package com.calclab.emite.xep.mucchatstate.client;

import java.util.logging.Logger;

import com.calclab.emite.im.client.chat.Chat;
import com.calclab.emite.im.client.chat.events.ChatChangedEvent;
import com.calclab.emite.im.client.chat.events.ChatChangedHandler;
import com.calclab.emite.xep.muc.client.Room;
import com.calclab.emite.xep.muc.client.RoomManager;
import com.google.inject.Inject;

/**
 * XEP-0085: Chat State Notifications
 * http://www.xmpp.org/extensions/xep-0085.html (Version: 1.2)
 * 
 * Chat state in MUC rooms. Note: Use of chat state notifications in the context
 * of groupchat can result in multicasting of such notifications. Forewarned is
 * forearmed.
 * 
 */
public class MUCChatStateManager {
	
	private static final Logger logger = Logger.getLogger(MUCChatStateManager.class.getName());
	
	@Inject
	public MUCChatStateManager(final RoomManager chatManager) {

		chatManager.addChatChangedHandler(new ChatChangedHandler() {
			@Override
			public void onChatChanged(final ChatChangedEvent event) {
				if (event.isCreated()) {
					getRoomOccupantsChatStateManager((Room) event.getChat());
				} else if (event.isClosed()) {
					final Chat chat = event.getChat();
					logger.finer("Removing chat state to chat: " + chat.getID());
					chat.getProperties().setData(RoomChatStateManager.KEY, null);
				}
			}
		});
	}

	public RoomChatStateManager getRoomOccupantsChatStateManager(final Room room) {

		RoomChatStateManager stateManager = (RoomChatStateManager) room.getProperties().getData(RoomChatStateManager.KEY);
		if (stateManager == null) {
			stateManager = createChatState(room);
		}
		return stateManager;
	}

	private RoomChatStateManager createChatState(final Room room) {
		logger.finer("Adding chat state to chat: " + room.getID());
		final RoomChatStateManager stateManager = new RoomChatStateManager(room);
		room.getProperties().setData(RoomChatStateManager.KEY, stateManager);
		return stateManager;
	}

}
