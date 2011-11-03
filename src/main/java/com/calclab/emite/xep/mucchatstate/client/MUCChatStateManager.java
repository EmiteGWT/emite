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

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;
import java.util.logging.Logger;

import com.calclab.emite.xep.muc.client.RoomChat;
import com.calclab.emite.xep.muc.client.RoomChatManager;
import com.calclab.emite.xep.muc.client.events.RoomChatChangedEvent;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.web.bindery.event.shared.EventBus;

/**
 * XEP-0085: Chat State Notifications
 * http://www.xmpp.org/extensions/xep-0085.html (Version: 1.2)
 * 
 * Chat state in MUC rooms. Note: Use of chat state notifications in the context
 * of groupchat can result in multicasting of such notifications. Forewarned is
 * forearmed.
 * 
 */
@Singleton
public final class MUCChatStateManager implements RoomChatChangedEvent.Handler {

	private static final Logger logger = Logger.getLogger(MUCChatStateManager.class.getName());

	private final EventBus eventBus;
	private final Map<RoomChat, MUCChatStateHook> hooks;

	@Inject
	protected MUCChatStateManager(@Named("emite") final EventBus eventBus, final RoomChatManager chatManager) {
		this.eventBus = checkNotNull(eventBus);
		hooks = Maps.newHashMap();
		
		chatManager.addRoomChatChangedHandler(this);
	}

	@Override
	public final void onRoomChatChanged(final RoomChatChangedEvent event) {
		final RoomChat room = event.getChat();
		if (event.isCreated()) {
			getRoomStateHook(room);
		} else if (event.isClosed()) {
			logger.finer("Removing chat state to chat: " + room.toString());
			hooks.remove(room);
		}
	}

	public final MUCChatStateHook getRoomStateHook(final RoomChat room) {
		MUCChatStateHook hook = hooks.get(room);
		if (hook == null) {
			logger.finer("Adding chat state to chat: " + room.toString());
			hook = new MUCChatStateHook(eventBus, room);
			hooks.put(room, hook);
		}
		return hook;
	}

}
