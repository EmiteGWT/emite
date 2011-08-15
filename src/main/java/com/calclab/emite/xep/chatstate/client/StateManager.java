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

package com.calclab.emite.xep.chatstate.client;

import java.util.logging.Logger;

import com.calclab.emite.im.client.chat.Chat;
import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.emite.im.client.chat.events.ChatChangedEvent;
import com.calclab.emite.im.client.chat.events.ChatChangedHandler;
import com.google.inject.Inject;

/**
 * XEP-0085: Chat State Notifications
 * http://www.xmpp.org/extensions/xep-0085.html (Version: 1.2)
 * 
 * This implementation is limited to chat conversations. Chat state in MUC rooms
 * are not supported to avoid multicast of occupant states (in a BOSH medium can
 * be a problem).
 * 
 */
public class StateManager {
	
	private static final Logger logger = Logger.getLogger(StateManager.class.getName());

	@Inject
	public StateManager(final ChatManager chatManager) {

		chatManager.addChatChangedHandler(new ChatChangedHandler() {
			@Override
			public void onChatChanged(final ChatChangedEvent event) {
				if (event.isCreated()) {
					getChatState(event.getChat());
				} else if (event.isClosed()) {
					final Chat chat = event.getChat();
					logger.finer("Removing chat state to chat: " + chat.getID());
					final ChatStateManager chatStateManager = (ChatStateManager) chat.getProperties().getData(ChatStateManager.KEY);
					if (chatStateManager != null && chatStateManager.getOtherState() != ChatStateManager.ChatState.gone) {
						// We are closing, then we send the gone state
						chatStateManager.setOwnState(ChatStateManager.ChatState.gone);
					}
					chat.getProperties().setData(ChatStateManager.KEY, null);
				}
			}
		});

	}

	public ChatStateManager getChatState(final Chat chat) {
		ChatStateManager chatStateManager = (ChatStateManager) chat.getProperties().getData(ChatStateManager.KEY);
		if (chatStateManager == null) {
			chatStateManager = createChatState(chat);
		}
		return chatStateManager;
	}

	private ChatStateManager createChatState(final Chat chat) {
		logger.finer("Adding chat state to chat: " + chat.getID());
		final ChatStateManager chatStateManager = new ChatStateManager(chat);
		chat.getProperties().setData(ChatStateManager.KEY, chatStateManager);
		return chatStateManager;
	}

}
