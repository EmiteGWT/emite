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

import com.calclab.emite.im.client.chat.pair.PairChat;
import com.calclab.emite.im.client.chat.pair.PairChatManager;
import com.calclab.emite.im.client.events.PairChatChangedEvent;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.web.bindery.event.shared.EventBus;

/**
 * XEP-0085: Chat State Notifications
 * http://www.xmpp.org/extensions/xep-0085.html (Version: 1.2)
 * 
 * This implementation is limited to chat conversations. Chat state in MUC rooms
 * are not supported to avoid multicast of occupant states (in a BOSH medium can
 * be a problem).
 * 
 */
@Singleton
public class ChatStateManager implements PairChatChangedEvent.Handler {

	private static final Logger logger = Logger.getLogger(ChatStateManager.class.getName());

	private final EventBus eventBus;

	@Inject
	public ChatStateManager(@Named("emite") final EventBus eventBus, final PairChatManager chatManager) {
		this.eventBus = eventBus;

		chatManager.addPairChatChangedHandler(this);
	}

	@Override
	public void onPairChatChanged(final PairChatChangedEvent event) {
		if (event.isCreated()) {
			getChatState(event.getChat());
		} else if (event.isClosed()) {
			final PairChat chat = event.getChat();
			logger.finer("Removing chat state to chat: " + chat.getID());
			final ChatStateHook chatStateHook = (ChatStateHook) chat.getProperties().getData(ChatStateHook.KEY);
			if (chatStateHook != null && chatStateHook.getOtherState() != ChatStateHook.ChatState.gone) {
				// We are closing, then we send the gone state
				chatStateHook.setOwnState(ChatStateHook.ChatState.gone);
			}
			chat.getProperties().setData(ChatStateHook.KEY, null);
		}
	}

	public ChatStateHook getChatState(final PairChat chat) {
		ChatStateHook chatStateManager = (ChatStateHook) chat.getProperties().getData(ChatStateHook.KEY);
		if (chatStateManager == null) {
			chatStateManager = createChatState(chat);
		}
		return chatStateManager;
	}

	private ChatStateHook createChatState(final PairChat chat) {
		logger.finer("Adding chat state to chat: " + chat.getID());
		final ChatStateHook chatStateManager = new ChatStateHook(eventBus, chat);
		chat.getProperties().setData(ChatStateHook.KEY, chatStateManager);
		return chatStateManager;
	}

}
