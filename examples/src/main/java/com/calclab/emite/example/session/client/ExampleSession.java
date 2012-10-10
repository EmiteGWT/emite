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

package com.calclab.emite.example.session.client;

import static com.calclab.emite.core.XmppURI.uri;

import com.calclab.emite.core.events.MessageReceivedEvent;
import com.calclab.emite.core.events.PresenceReceivedEvent;
import com.calclab.emite.core.events.SessionStatusChangedEvent;
import com.calclab.emite.core.session.SessionStatus;
import com.calclab.emite.core.session.XmppSession;
import com.calclab.emite.core.stanzas.Message;
import com.calclab.emite.core.stanzas.Presence;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Xmpp Session Example
 */
public class ExampleSession implements EntryPoint, SessionStatusChangedEvent.Handler, MessageReceivedEvent.Handler, PresenceReceivedEvent.Handler {

	private static final ExampleSessionGinjector ginjector = GWT.create(ExampleSessionGinjector.class);
	private final XmppSession session = ginjector.getXmppSession();
	
	private VerticalPanel output;

	@Override
	public void onModuleLoad() {
		output = new VerticalPanel();
		RootLayoutPanel.get().add(output);
		log("Emite example: xmpp sessions");

		/*
		 * We track session state changes. We can only send messages
		 * when the state == loggedIn.
		 */
		session.addSessionStatusChangedHandler(this, true);

		/*
		 * We show every incoming message in the GWT log console
		 */
		session.addMessageReceivedHandler(this);

		/*
		 * We show (log) every incoming presence stanzas
		 */
		session.addPresenceReceivedHandler(this);
	}
	
	@Override
	public void onSessionStatusChanged(final SessionStatusChangedEvent event) {
		if (SessionStatus.loggedIn.equals(event.getStatus())) {
			log("We are now online");
			sendHelloWorldMessage();
		} else if (SessionStatus.disconnected.equals(event.getStatus())) {
			log("We are now offline");
		} else {
			log("Current state: " + event.getStatus());
		}
	}
	
	@Override
	public void onMessageReceived(final MessageReceivedEvent event) {
		final Message message = event.getMessage();
		log("Messaged received from " + message.getFrom().toString() + ":" + message.getBody());
	}
	
	@Override
	public void onPresenceReceived(final PresenceReceivedEvent event) {
		final Presence presence = event.getPresence();
		log("Presence received from " + presence.getFrom().toString() + ": " + presence.toString());
	}

	private void log(final String text) {
		output.add(new Label(text));
	}

	/**
	 * The simplest way to send a message using the Session object
	 */
	private void sendHelloWorldMessage() {
		final Message message = new Message("Hello world!");
		message.setTo(uri("test3@localhost"));
		session.send(message);
	}
}
