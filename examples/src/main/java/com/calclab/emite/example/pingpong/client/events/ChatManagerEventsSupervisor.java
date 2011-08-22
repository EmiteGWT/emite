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

import com.calclab.emite.core.client.events.ErrorEvent;
import com.calclab.emite.core.client.events.ChangedEvent.ChangeType;
import com.calclab.emite.im.client.chat.ChatStateChangedEvent;
import com.calclab.emite.im.client.chat.pair.PairChat;
import com.calclab.emite.im.client.chat.pair.PairChatChangedEvent;
import com.calclab.emite.im.client.chat.pair.PairChatManager;
import com.calclab.emite.example.pingpong.client.PingPongDisplay;
import com.calclab.emite.example.pingpong.client.PingPongDisplay.Style;
import com.google.inject.Inject;

public class ChatManagerEventsSupervisor implements PairChatChangedEvent.Handler {

	private final PingPongDisplay display;
	
	@Inject
	public ChatManagerEventsSupervisor(final PairChatManager chatManager, final PingPongDisplay display) {
		this.display = display;
		
		chatManager.addPairChatChangedHandler(this);
	}
	
	@Override
	public void onPairChatChanged(final PairChatChangedEvent event) {
		final PairChat chat = event.getChat();
		display.print("CHAT CHANGED " + chat.getURI() + " - " + event.getChangeType(), Style.event);
		
		if (event.is(ChangeType.created)) {
			chat.addChatStateChangedHandler(false, new ChatStateChangedEvent.Handler() {
				@Override
				public void onChatStateChanged(final ChatStateChangedEvent event) {
					display.print("CHAT STATE " + chat.getURI() + " changed: " + event.getState(), Style.event);
				}
			});
			
			display.print("CHAT STATE " + chat.getURI() + " - " + chat.getChatState(), Style.event);
			chat.addErrorHandler(new ErrorEvent.Handler() {
				@Override
				public void onError(final ErrorEvent event) {
					final String stanza = event.getStanza() != null ? event.getStanza().toString() : "(no stanza)";
					display.print("CHAT ERROR " + chat.getURI() + ": " + event.getErrorType() + "- " + event.getDescription() + ": " + stanza, Style.error);
				}
			});
		}
	}
	
}
