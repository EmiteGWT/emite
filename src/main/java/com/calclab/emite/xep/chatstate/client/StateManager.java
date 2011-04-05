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
package com.calclab.emite.xep.chatstate.client;

import com.calclab.emite.im.client.chat.Chat;
import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.emite.im.client.chat.events.ChatChangedEvent;
import com.calclab.emite.im.client.chat.events.ChatChangedHandler;
import com.google.gwt.core.client.GWT;
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

    @Inject
    public StateManager(final ChatManager chatManager) {

	chatManager.addChatChangedHandler(new ChatChangedHandler() {
	    @Override
	    public void onChatChanged(ChatChangedEvent event) {
		if (event.isCreated()) {
		    getChatState(event.getChat());
		} else if (event.isClosed()) {
		    Chat chat = event.getChat();
		    GWT.log("Removing chat state to chat: " + chat.getID(), null);
		    final ChatStateManager chatStateManager = (ChatStateManager) chat.getProperties().getData(
			    ChatStateManager.KEY);
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
	GWT.log("Adding chat state to chat: " + chat.getID(), null);
	final ChatStateManager chatStateManager = new ChatStateManager(chat);
	chat.getProperties().setData(ChatStateManager.KEY, chatStateManager);
	return chatStateManager;
    }

}
