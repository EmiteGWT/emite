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

package com.calclab.emite.im.client.chat;

import com.calclab.emite.core.client.events.ChangedEvent.ChangeTypes;
import com.calclab.emite.core.client.events.MessageEvent;
import com.calclab.emite.core.client.events.MessageHandler;
import com.calclab.emite.core.client.events.StateChangedEvent;
import com.calclab.emite.core.client.events.StateChangedHandler;
import com.calclab.emite.core.client.xmpp.session.SessionStates;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.events.ChatChangedEvent;

public abstract class AbstractChatManager extends ChatManagerBoilerplate {
	private XmppURI currentChatUser;

	public AbstractChatManager(final XmppSession session, final ChatSelectionStrategy strategy) {
		super(session, strategy);
		forwardMessagesToChats();
		controlSessionStatus();
	}

	@Override
	public void close(final Chat chat) {
		chat.close();
		getChats().remove(chat);
		managerEventBus.fireEvent(new ChatChangedEvent(ChangeTypes.closed, chat));
	}

	@Override
	public Chat getChat(final ChatProperties properties, final boolean createIfNotFound) {
		for (final Chat chat : chats) {
			if (strategy.isAssignable(chat.getProperties(), properties))
				return chat;
		}
		if (createIfNotFound) {
		}
		return null;
	}

	@Override
	public Chat openChat(final ChatProperties properties, final boolean createIfNotFound) {
		Chat chat = getChat(properties, false);
		if (chat == null) {
			if (!createIfNotFound)
				return null;
			properties.setInitiatorUri(session.getCurrentUserURI());
			chat = addNewChat(properties);
		}
		chat.open();
		managerEventBus.fireEvent(new ChatChangedEvent(ChangeTypes.opened, chat));
		return chat;
	}

	protected void addChat(final Chat chat) {
		chats.add(chat);
	}

	/**
	 * This method creates a new chat, add it to the pool and fire the event
	 * 
	 * @param properties
	 */
	protected Chat addNewChat(final ChatProperties properties) {
		final Chat chat = createChat(properties);
		addChat(chat);
		managerEventBus.fireEvent(new ChatChangedEvent(ChangeTypes.created, chat));
		return chat;
	}

	/**
	 * A template method: the subclass must return a new object of class Chat
	 * 
	 * @param properties
	 *            the properties of the chat
	 * @return a new chat. must not be null
	 */
	protected abstract Chat createChat(ChatProperties properties);

	private void controlSessionStatus() {
		// Control chat state when the user logout and login again
		session.addSessionStateChangedHandler(true, new StateChangedHandler() {
			@Override
			public void onStateChanged(final StateChangedEvent event) {
				if (event.is(SessionStates.loggedIn)) {
					final XmppURI currentUser = session.getCurrentUserURI();
					if (currentChatUser == null) {
						currentChatUser = currentUser;
					}
					if (currentUser.equalsNoResource(currentChatUser)) {
						for (final Chat chat : chats) {
							chat.open();
						}
					}
				} else if (event.is(SessionStates.loggingOut) || event.is(SessionStates.disconnected)) {
					// check both states: loggingOut is preferred, but not
					// always fired (i.e. error)
					for (final Chat chat : chats) {
						chat.close();
					}
				}
			}
		});

	}

	private void forwardMessagesToChats() {
		session.addMessageReceivedHandler(new MessageHandler() {
			@Override
			public void onMessage(final MessageEvent event) {
				final Message message = event.getMessage();
				final ChatProperties properties = strategy.extractProperties(message);
				if (properties != null) {
					Chat chat = getChat(properties, false);
					if (chat == null && properties.shouldCreateNewChat()) {
						// we need to create a chat for this incoming message
						properties.setInitiatorUri(properties.getUri());
						chat = addNewChat(properties);
					}
					if (chat != null) {
						chat.receive(message);
					}
				}
			}
		});
	}

}
