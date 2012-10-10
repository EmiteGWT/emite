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

import static com.calclab.emite.core.XmppURI.uri;

import com.calclab.emite.browser.PageAssist;
import com.calclab.emite.core.events.MessageReceivedEvent;
import com.calclab.emite.core.events.SessionStatusChangedEvent;
import com.calclab.emite.core.session.XmppSession;
import com.calclab.emite.core.stanzas.Message;
import com.calclab.emite.im.chat.PairChat;
import com.calclab.emite.im.chat.PairChatManager;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * A simple echo client
 */
public class ExampleEcho implements EntryPoint, SessionStatusChangedEvent.Handler, MessageReceivedEvent.Handler {

	private static final ExampleEchoGinjector ginjector = GWT.create(ExampleEchoGinjector.class);

	private final XmppSession session = ginjector.getXmppSession();
	private final PairChatManager chatManager = ginjector.getPairChatManager();
	
	private PairChat chat;
	private VerticalPanel output;

	@Override
	public void onModuleLoad() {
		output = new VerticalPanel();
		RootLayoutPanel.get().add(output);

		log("Example echo chat");
		final String self = PageAssist.getMeta("emite.user", null);
		log("Current user: " + self);
		final String user = PageAssist.getMeta("emite.chat", null);
		log("Chat with user: " + user);

		session.addSessionStatusChangedHandler(this, true);

		chat = chatManager.openChat(uri(user));
		chat.addMessageReceivedHandler(this);
	}
	
	@Override
	public void onSessionStatusChanged(final SessionStatusChangedEvent event) {
		log("Current status: " + event.getStatus().toString());
	}
	
	@Override
	public void onMessageReceived(final MessageReceivedEvent event) {
		final String body = event.getMessage().getBody();
		log("Message received: " + body);
		chat.send(new Message(body + " at: " + System.currentTimeMillis()));
	}

	private void log(final String text) {
		output.add(new Label(text));
	}

}
