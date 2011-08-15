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

package com.calclab.emite.example.pingpong.client.events;

import com.calclab.emite.core.client.events.ChangedEvent.ChangeTypes;
import com.calclab.emite.core.client.events.ErrorEvent;
import com.calclab.emite.core.client.events.ErrorHandler;
import com.calclab.emite.core.client.events.StateChangedEvent;
import com.calclab.emite.core.client.events.StateChangedHandler;
import com.calclab.emite.im.client.chat.Chat;
import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.emite.im.client.chat.events.ChatChangedEvent;
import com.calclab.emite.im.client.chat.events.ChatChangedHandler;
import com.calclab.emite.example.pingpong.client.PingPongDisplay;
import com.calclab.emite.example.pingpong.client.PingPongDisplay.Style;
import com.google.inject.Inject;

public class ChatManagerEventsSupervisor {

	@Inject
	public ChatManagerEventsSupervisor(final ChatManager chatManager, final PingPongDisplay display) {
		chatManager.addChatChangedHandler(new ChatChangedHandler() {
			@Override
			public void onChatChanged(final ChatChangedEvent event) {
				display.print("CHAT CHANGED " + event.getChat().getURI() + " - " + event.getChangeType(), Style.event);
				if (event.is(ChangeTypes.created)) {
					trackChat(event.getChat(), display);
				}
			}
		});
	}

	protected void trackChat(final Chat chat, final PingPongDisplay output) {
		chat.addChatStateChangedHandler(false, new StateChangedHandler() {
			@Override
			public void onStateChanged(final StateChangedEvent event) {
				output.print("CHAT STATE " + chat.getURI() + " changed: " + event.getState(), Style.event);
			}
		});
		output.print("CHAT STATE " + chat.getURI() + " - " + chat.getChatState(), Style.event);

		chat.addErrorHandler(new ErrorHandler() {
			@Override
			public void onError(final ErrorEvent event) {
				final String stanza = event.getStanza() != null ? event.getStanza().toString() : "(no stanza)";
				output.print("CHAT ERROR " + chat.getURI() + ": " + event.getErrorType() + "- " + event.getDescription() + ": " + stanza, Style.error);
			}
		});
	}

}
