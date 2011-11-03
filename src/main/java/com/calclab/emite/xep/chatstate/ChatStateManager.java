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

package com.calclab.emite.xep.chatstate;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;
import java.util.logging.Logger;

import com.calclab.emite.im.chat.PairChat;
import com.calclab.emite.im.chat.PairChatManager;
import com.calclab.emite.im.events.PairChatChangedEvent;
import com.google.common.collect.Maps;
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
 */
@Singleton
public final class ChatStateManager implements PairChatChangedEvent.Handler {

	private static final Logger logger = Logger.getLogger(ChatStateManager.class.getName());

	private final EventBus eventBus;
	private final Map<PairChat, ChatStateHook> hooks;

	@Inject
	protected ChatStateManager(@Named("emite") final EventBus eventBus, final PairChatManager chatManager) {
		this.eventBus = checkNotNull(eventBus);
		this.hooks = Maps.newHashMap();

		chatManager.addPairChatChangedHandler(this);
	}

	@Override
	public final void onPairChatChanged(final PairChatChangedEvent event) {
		if (event.isCreated()) {
			getChatStateHook(event.getChat());
		} else if (event.isClosed()) {
			final PairChat chat = event.getChat();
			logger.finer("Removing chat state from chat: " + chat.toString());
			final ChatStateHook hook = hooks.get(chat);
			if (hook != null && hook.getOtherState() != ChatStateHook.ChatState.gone) {
				// We are closing, then we send the gone state
				hook.setOwnState(ChatStateHook.ChatState.gone);
			}
			hooks.remove(chat);
		}
	}

	public final ChatStateHook getChatStateHook(final PairChat chat) {
		ChatStateHook hook = hooks.get(checkNotNull(chat));
		if (hook == null) {
			logger.finer("Adding chat state to chat: " + chat.toString());
			hook = new ChatStateHook(eventBus, chat);
			hooks.put(chat, hook);
		}
		return hook;
	}

}
