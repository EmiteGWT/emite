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

package com.calclab.emite.example.pingpong.client.logic;

import com.calclab.emite.core.client.events.ChangedEvent.ChangeTypes;
import com.calclab.emite.core.client.events.MessageEvent;
import com.calclab.emite.core.client.events.MessageHandler;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.im.client.chat.Chat;
import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.emite.im.client.chat.events.ChatChangedEvent;
import com.calclab.emite.im.client.chat.events.ChatChangedHandler;
import com.calclab.emite.example.pingpong.client.PingPongDisplay;
import com.calclab.emite.example.pingpong.client.PingPongDisplay.Style;
import com.calclab.emite.example.pingpong.client.StartablePresenter;
import com.calclab.emite.example.pingpong.client.events.ChatManagerEventsSupervisor;
import com.google.inject.Inject;

public class PongChatPresenter implements StartablePresenter {

	private final PingPongDisplay display;
	private int pongs;
	private final ChatManager chatManager;

	@Inject
	public PongChatPresenter(final ChatManager chatManager, final PingPongDisplay output) {
		this.chatManager = chatManager;
		display = output;
		pongs = 0;
	}

	@Override
	public void start() {
		display.printHeader("This is pong chat", Style.title);
		display.printHeader("You need to open the ping chat example page in order to run the example", Style.important);

		new ChatManagerEventsSupervisor(chatManager, display);
		chatManager.addChatChangedHandler(new ChatChangedHandler() {
			@Override
			public void onChatChanged(final ChatChangedEvent event) {
				if (event.is(ChangeTypes.created)) {
					final Chat chat = event.getChat();
					listenToChat(chat);
				}
			}
		});
	}

	private void listenToChat(final Chat chat) {
		chat.addMessageReceivedHandler(new MessageHandler() {
			@Override
			public void onMessage(final MessageEvent event) {
				final Message message = event.getMessage();
				display.print(("RECEIVED: " + message.getBody()), Style.received);
				pongs++;
				final String body = "Pong " + pongs + " [" + System.currentTimeMillis() + "]";
				chat.send(new Message(body));
				display.print("SENT: " + body, Style.sent);

			}
		});
	}

}
