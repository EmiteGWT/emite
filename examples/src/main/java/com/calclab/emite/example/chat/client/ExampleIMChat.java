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

package com.calclab.emite.example.chat.client;

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
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ExampleIMChat implements EntryPoint {

	private VerticalPanel output;

	private TextBox input;

	@Override
	public void onModuleLoad() {
		createUI();

		log("Example IM Chat");
		final String self = PageAssist.getMeta("emite.user");
		log("Current user: " + self);
		final String user = PageAssist.getMeta("emite.chat");
		log("Chat with user: " + user);

		final ExampleIMChatGinjector ginjector = GWT.create(ExampleIMChatGinjector.class);
		final XmppSession session = ginjector.getXmppSession();

		session.addSessionStateChangedHandler(true, new StateChangedHandler() {
			@Override
			public void onStateChanged(final StateChangedEvent event) {
				final String state = event.getState();
				log("Current state: " + state);
			}
		});

		final ChatManager chatManager = ginjector.getChatManager();
		input.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(final ChangeEvent event) {
				final String msg = input.getText();
				log("Message sent: " + msg);
				final Chat chat = chatManager.open(uri(user));
				chat.send(new Message(msg));
				input.setText("");
			}
		});

		final Chat chat = chatManager.open(uri(user));
		chat.addMessageReceivedHandler(new MessageHandler() {
			@Override
			public void onMessage(final MessageEvent event) {
				log("Message received: " + event.getMessage().getBody());
			}
		});
	}

	private void createUI() {
		final DockLayoutPanel dock = new DockLayoutPanel(Unit.PX);
		input = new TextBox();
		dock.addSouth(input, 50);
		output = new VerticalPanel();
		dock.add(output);
		RootLayoutPanel.get().add(dock);
	}

	private void log(final String text) {
		output.add(new Label(text));
	}

}
