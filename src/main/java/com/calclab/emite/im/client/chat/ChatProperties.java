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

import java.util.HashMap;
import java.util.Map;

import com.calclab.emite.core.client.uri.XmppURI;

/**
 * The properties of a chat. One property is required (the uri), some other are
 * fixed but optional (like the initiatorUri, or the status) and some are
 * optional and not fixed (you can add or retrieve any property by key)
 * 
 * The properties are the id used to find a chat in a pool of chats.
 */
public class ChatProperties {
	private final Map<String, Object> data;
	private final XmppURI uri;
	private XmppURI initiatorUri;
	private ChatStatus status;
	private boolean shouldCreateNewChat;

	public ChatProperties(final XmppURI uri) {
		this(uri, null, null);
	}

	/**
	 * Creates a new {@link ChatProperties} instance taking the data from the
	 * given properties instance. Note that this will not copy the initiator uri
	 * or status.
	 * 
	 * @param uri
	 *            the new uri for the properties.
	 * @param properties
	 *            the properties object to replicate.
	 */
	public ChatProperties(final XmppURI uri, final ChatProperties properties) {
		this(uri, null, null, properties);

		for (final Map.Entry<String, Object> entry : properties.data.entrySet()) {
			setData(entry.getKey(), entry.getValue());
		}
	}

	public ChatProperties(final XmppURI uri, final XmppURI initiatorUri, final ChatStatus status) {
		this.uri = uri;
		this.initiatorUri = initiatorUri;
		this.status = status;
		shouldCreateNewChat = true;
		data = new HashMap<String, Object>();
	}

	/**
	 * Creates a new {@link ChatProperties} instance taking the data from the
	 * given properties instance. Note that this will not copy the initiator uri
	 * or status.
	 * 
	 * @param uri
	 *            the new uri for the properties.
	 * @param initiatorUri
	 *            the uri of the chat initiator.
	 * @param status
	 *            the chat status.
	 * @param properties
	 *            the properties object to replicate.
	 */
	public ChatProperties(final XmppURI uri, final XmppURI initiatorUri, final ChatStatus status, final ChatProperties properties) {
		this(uri, initiatorUri, status);

		for (final Map.Entry<String, Object> entry : properties.data.entrySet()) {
			setData(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * Get the metadata object associated to a given key
	 * 
	 * @param key
	 *            the key
	 * @return the associated object if any, null otherwise
	 * @see setData
	 */
	public Object getData(final String key) {
		return data.get(key);
	}

	/**
	 * This is the uri of the entity that inititated the chat
	 * 
	 * @return
	 */
	public XmppURI getInitiatorUri() {
		return initiatorUri;
	}

	/**
	 * The current status of the chat. See ChatStatus to see some options.
	 * 
	 * @return
	 * @see ChatStatus
	 */
	public ChatStatus getStatus() {
		return status;
	}

	/**
	 * Get the chats uri. Is the other side of the conversation in a PairChat,
	 * or the room uri in a RoomChat
	 * 
	 * @return
	 */
	public XmppURI getUri() {
		return uri;
	}

	/**
	 * Set the metadata object associated to a given key
	 * 
	 * @param key
	 *            the key
	 * @param data
	 *            the object you want to associate
	 * @return the previously metadata object associated to that kay (if any) or
	 *         null otherwise
	 * @see getData
	 */
	public Object setData(final String key, final Object value) {
		return data.put(key, value);
	}

	/**
	 * Change the initiatorUri
	 * 
	 * @param initiatorUri
	 */
	public void setInitiatorUri(final XmppURI initiatorUri) {
		this.initiatorUri = initiatorUri;
	}

	/**
	 * If this attribute is true, when a chat is not found with this properties,
	 * the ChatManager should create a new chat.
	 * 
	 * If this attribute is false, this chat properties never fire a new chat
	 * creation
	 * 
	 * @param shouldCreateNewChat
	 */
	public void setShouldCreateNewChat(final boolean shouldCreateNewChat) {
		this.shouldCreateNewChat = shouldCreateNewChat;
	}

	/**
	 * Change the status of the chat
	 * 
	 * @param status
	 */
	public void setStatus(final ChatStatus status) {
		this.status = status;
	}

	/**
	 * ChatManager uses this value to know whenever a new chat should be created
	 * when a message with this chat properties arrives
	 * 
	 * @return
	 */
	public boolean shouldCreateNewChat() {
		return shouldCreateNewChat;
	}
}
