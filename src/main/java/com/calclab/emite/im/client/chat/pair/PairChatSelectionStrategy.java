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

package com.calclab.emite.im.client.chat.pair;

import com.calclab.emite.core.client.stanzas.Stanza;
import com.calclab.emite.im.client.chat.ChatProperties;
import com.calclab.emite.im.client.chat.ChatSelectionStrategy;
import com.google.inject.Singleton;

/**
 * A simple chat provider strategy. It ignores the metadata and the resource of
 * the user. This is the default strategy for a ChatManager
 */
@Singleton
public class PairChatSelectionStrategy implements ChatSelectionStrategy {

	@Override
	public ChatProperties extractProperties(final Stanza stanza) {
		final ChatProperties properties = new ChatProperties(stanza.getFrom());
		boolean shouldCreate = stanza.getXML().hasChild("body") && !"groupchat".equals(stanza.getXML().getAttribute("type"));
		shouldCreate = shouldCreate && stanza.getXML().hasChild("x", "http://jabber.org/protocol/muc#user");
		properties.setShouldCreateNewChat(shouldCreate);
		return properties;
	}

	@Override
	public boolean isAssignable(final ChatProperties chatProperties, final ChatProperties properties) {
		return chatProperties.getUri().equalsNoResource(properties.getUri());
	}

}
