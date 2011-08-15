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

package com.calclab.emite.example.echo.client;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;

import com.calclab.emite.browser.client.PageAssist;
import com.calclab.emite.core.client.events.MessageEvent;
import com.calclab.emite.core.client.events.MessageHandler;
import com.calclab.emite.core.client.events.StateChangedEvent;
import com.calclab.emite.core.client.events.StateChangedHandler;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.im.client.chat.Chat;
import com.calclab.emite.im.client.chat.ChatManager;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * A simple echo client
 */
public class ExampleEcho implements EntryPoint {

	private VerticalPanel output;

	@Override
	public void onModuleLoad() {
		output = new VerticalPanel();
		RootLayoutPanel.get().add(output);

		log("Example echo chat");
		final String self = PageAssist.getMeta("emite.user");
		log("Current user: " + self);
		final String user = PageAssist.getMeta("emite.chat");
		log("Chat with user: " + user);

		final EchoGinjector ginjector = GWT.create(EchoGinjector.class);

		final XmppSession session = ginjector.getXmppSession();

		session.addSessionStateChangedHandler(true, new StateChangedHandler() {
			@Override
			public void onStateChanged(final StateChangedEvent event) {
				final String state = event.getState();
				log("Current state: " + state);
			}
		});

		final ChatManager chatManager = ginjector.getChatManager();
		final Chat chat = chatManager.open(uri(user));
		chat.addMessageReceivedHandler(new MessageHandler() {
			@Override
			public void onMessage(final MessageEvent event) {
				final String body = event.getMessage().getBody();
				log("Message received: " + body);
				chat.send(new Message(body + " at: " + System.currentTimeMillis()));
			}
		});
	}

	private void log(final String text) {
		output.add(new Label(text));
	}

}
