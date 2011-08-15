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
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.example.pingpong.client.PingPongDisplay;
import com.calclab.emite.example.pingpong.client.PingPongDisplay.Style;
import com.calclab.emite.example.pingpong.client.StartablePresenter;
import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * Receives pings (and answer pongs) using the old Session object
 * 
 */
public class PongSessionPresenter implements StartablePresenter {

	private final XmppURI other;
	private final PingPongDisplay output;
	protected int pongs;
	private final XmppSession session;

	@Inject
	public PongSessionPresenter(final XmppSession session, @Named("other") final XmppURI other, final PingPongDisplay output) {
		this.session = session;
		this.other = other;
		this.output = output;
		pongs = 0;
	}

	@Override
	public void start() {
		output.printHeader("This is pong session example", Style.title);
		output.printHeader("Pong to: " + other, Style.info);
		output.print("You need to open the ping example page in order to run the example", Style.important);

		session.addMessageReceivedHandler(new MessageHandler() {
			@Override
			public void onMessage(final MessageEvent event) {
				final Message message = event.getMessage();
				output.print(("RECEIVED: " + message.getBody()), Style.received);
				pongs++;
				final String body = "Pong " + pongs + " [" + System.currentTimeMillis() + "]";
				session.send(new Message(body, other));
				output.print("SENT: " + body, Style.sent);
			}
		});

	}

}
