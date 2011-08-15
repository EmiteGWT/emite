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

import com.calclab.emite.core.client.xmpp.stanzas.BasicStanza;

/**
 * An interchangeable strategy to retrieve or create new chats. You can replace
 * one strategy with other in the ChatManager.
 * 
 * The strategy can extract ChatProperties from a stanza (like external
 * metadata) and helps to find the target chat comparing properties from the
 * stanza from the properties of the chat
 * 
 */
public interface ChatSelectionStrategy {

	/**
	 * Extract the properties of the given stanza. This properties are used to
	 * find a chat AND decide whenever or not create a new one if no one is
	 * found
	 * 
	 * @param stanza
	 *            the stanza to extract the properties from (usually a message,
	 *            but can be presence in rooms)
	 * @return the properties of the ideal receiver of this message (stanza). If
	 *         null is return, the ChatManager assume this message has no
	 *         receiver
	 */
	ChatProperties extractProperties(BasicStanza stanza);

	/**
	 * This is the used to locate a chat in a pool of chats.
	 * 
	 * Should return true if the given chat properties correspond (or is
	 * asignable) to the message (or stanza) properties. This NOT mean the
	 * properties should be same between both.
	 * 
	 * Different "strategies" could implement different assigment techinques
	 * (ie. only comparing the uri)
	 * 
	 * @param chat
	 *            the chat to examined
	 * @param properties
	 *            the properties
	 * @return true if the chat matches the properties, else otherwise
	 */
	boolean isAssignable(ChatProperties chatProperties, ChatProperties messageProperties);

}
