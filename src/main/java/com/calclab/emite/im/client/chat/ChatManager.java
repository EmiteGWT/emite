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

import java.util.Collection;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.events.ChatChangedHandler;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Create and manage chat conversations.
 * 
 * There are one implementation for one-to-one conversations (PairChatManager)
 * and many-to-many conversations (RoomManagerImpl)
 */
public interface ChatManager {

	/**
	 * Add a handler to track chat changes. The following changes can ocurr from
	 * a default chat manager: created, opened, closed
	 * 
	 * @param handler
	 */
	public HandlerRegistration addChatChangedHandler(ChatChangedHandler handler);

	/**
	 * Close the given conversation. If a conversation is closed, a new
	 * onChatCreated event will be throw when opened
	 * 
	 * @param chat
	 */
	public void close(Chat chat);

	/**
	 * Obtain a chat from the chat manager. It can create new chats if
	 * specified.
	 * 
	 * @param properties
	 *            the properties of the chat
	 * @param createIfNotFound
	 *            if true, ChatManager will create a new chat with that
	 *            properties
	 * @return the chat with that properties. If createIfNotFound is false, the
	 *         return CAN be null
	 */
	public Chat getChat(ChatProperties properties, boolean createIfNotFound);

	/**
	 * Same as getChat(new ChatProperties(uri), false);
	 * 
	 * Here for compatibility reasons.
	 * 
	 * @param uri
	 * @return
	 */
	public Chat getChat(XmppURI uri);

	public Collection<? extends Chat> getChats();

	/**
	 * Same as openChat(new ChatProperties(uri))
	 * 
	 * Here for compatilibility reasons
	 * 
	 * @param uri
	 * @return
	 */
	// FIXME: deprecate
	public Chat open(XmppURI uri);

	/**
	 * The same as getChat, but it fire ChatChanged(opened) event if the chat is
	 * found or created
	 * 
	 * @param properties
	 * @param createIfNotFound
	 * @return
	 */
	public Chat openChat(ChatProperties properties, boolean createIfNotFound);

	/**
	 * Changes a the chat selection strategy of the current chat manager
	 * 
	 * @param strategy
	 *            the new strategy. can't be null
	 */
	void setChatSelectionStrategy(ChatSelectionStrategy strategy);

}
