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

import com.calclab.emite.core.client.events.MessageEvent;
import com.calclab.emite.core.client.events.MessageHandler;
import com.calclab.emite.core.client.events.StateChangedEvent;
import com.calclab.emite.core.client.events.StateChangedHandler;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.Chat;
import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.emite.im.client.chat.ChatStates;
import com.calclab.emite.example.pingpong.client.PingPongDisplay;
import com.calclab.emite.example.pingpong.client.PingPongDisplay.Style;
import com.calclab.emite.example.pingpong.client.StartablePresenter;
import com.calclab.emite.example.pingpong.client.events.ChatManagerEventsSupervisor;
import com.google.gwt.user.client.Timer;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class PingChatPresenter implements StartablePresenter {

	protected final XmppURI other;
	protected final PingPongDisplay display;
	protected int pings;
	protected int waitTime;
	private final ChatManager chatManager;

	@Inject
	public PingChatPresenter(final ChatManager chatManager, @Named("other") final XmppURI other, final PingPongDisplay output) {
		this.chatManager = chatManager;
		this.other = other;
		display = output;
		pings = 0;
		waitTime = 2000;
	}

	@Override
	public void start() {
		// OPEN THE CHAT
		display.printHeader("This is ping chat example", Style.title);
		display.printHeader("You need to open the pong example page in order to run the example", Style.important);

		display.printHeader("Ping to: " + other, Style.info);

		new ChatManagerEventsSupervisor(chatManager, display);
		final Chat chat = chatManager.open(other);

		chat.addMessageReceivedHandler(new MessageHandler() {
			@Override
			public void onMessage(final MessageEvent event) {
				display.print(("RECEIVED: " + event.getMessage().getBody()), Style.received);
			}
		});

		// SEND THE FIRST PING WHEN THE CHAT IS READY
		chat.addChatStateChangedHandler(true, new StateChangedHandler() {
			@Override
			public void onStateChanged(final StateChangedEvent event) {
				if (event.is(ChatStates.ready)) {
					sendPing(chat);
				}
			}

		});
	}

	protected void sendPing(final Chat chat) {
		if (chat.isReady()) {
			pings++;
			waitTime += 500;
			final String body = "Ping " + pings + " [" + System.currentTimeMillis() + "]";
			chat.send(new Message(body));
			display.print("SENT: " + body, Style.sent);
			new Timer() {
				@Override
				public void run() {
					sendPing(chat);
				}
			}.schedule(waitTime);
		}
	}

}
