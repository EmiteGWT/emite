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

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;

import java.util.logging.Logger;

import com.calclab.emite.core.client.events.MessageEvent;
import com.calclab.emite.core.client.events.MessageHandler;
import com.calclab.emite.core.client.events.PresenceEvent;
import com.calclab.emite.core.client.events.PresenceHandler;
import com.calclab.emite.core.client.events.StateChangedEvent;
import com.calclab.emite.core.client.events.StateChangedHandler;
import com.calclab.emite.core.client.xmpp.session.SessionStates;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.Presence;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Xmpp Session Example
 */
public class ExampleXmppSession implements EntryPoint {

	private static final Logger logger = Logger.getLogger(ExampleXmppSession.class.getName());
	
	private VerticalPanel panel;

	@Override
	public void onModuleLoad() {
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				panel = new VerticalPanel();
				RootLayoutPanel.get().add(panel);
				log("Emite example: xmpp sessions");

				/*
				 * We get the Session object using the ginjector. This is NOT
				 * the recommended way: it's better to inject into constructor
				 */
				logger.info("Create a xmpp session");
				final ExampleXmppSessionGinjector ginjector = GWT.create(ExampleXmppSessionGinjector.class);
				final XmppSession session = ginjector.getXmppSession();

				logger.info("Add event handlers");
				/*
				 * We track session state changes. We can only send messages
				 * when the state == loggedIn.
				 */
				session.addSessionStateChangedHandler(true, new StateChangedHandler() {
					@Override
					public void onStateChanged(final StateChangedEvent event) {
						if (event.is(SessionStates.loggedIn)) {
							log("We are now online");
							sendHelloWorldMessage(session);
						} else if (event.is(SessionStates.disconnected)) {
							log("We are now offline");
						} else {
							log("Current state: " + event.getState());
						}
					}
				});

				/*
				 * We show every incoming message in the GWT log console
				 */
				session.addMessageReceivedHandler(new MessageHandler() {
					@Override
					public void onMessage(final MessageEvent event) {
						final Message message = event.getMessage();
						log("Messaged received from " + message.getFrom() + ":" + message.getBody());
					}
				});

				/*
				 * We show (log) every incoming presence stanzas
				 */
				session.addPresenceReceivedHandler(new PresenceHandler() {

					@Override
					public void onPresence(final PresenceEvent event) {
						final Presence presence = event.getPresence();
						log("Presence received from " + presence.getFrom() + ": " + presence.toString());
					}
				});
			}
		});

	}

	private void log(final String text) {
		panel.add(new Label(text));
	}

	/**
	 * The simplest way to send a message using the Session object
	 */
	private void sendHelloWorldMessage(final XmppSession session) {
		final Message message = new Message("hello world!", uri("test3@localhost"));
		session.send(message);
	}
}
