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

import com.calclab.emite.core.client.packet.MatcherFactory;
import com.calclab.emite.core.client.packet.NoPacket;
import com.calclab.emite.core.client.packet.PacketMatcher;
import com.calclab.emite.core.client.xmpp.stanzas.BasicStanza;

/**
 * A simple chat provider strategy. It ignores the metadata and the resource of
 * the user. This is the default strategy for a ChatManager
 * 
 */
public class PairChatSelectionStrategy implements ChatSelectionStrategy {

	private final PacketMatcher mucMatcher = MatcherFactory.byNameAndXMLNS("x", "http://jabber.org/protocol/muc#user");

	@Override
	public ChatProperties extractProperties(final BasicStanza stanza) {
		final ChatProperties properties = new ChatProperties(stanza.getFrom());
		final boolean messageHasBody = stanza.getFirstChild("body") != NoPacket.INSTANCE;

		boolean shouldCreate = messageHasBody && !"groupchat".equals(stanza.getAttribute("type"));
		shouldCreate = shouldCreate && stanza.getFirstChildInDeep(mucMatcher) == NoPacket.INSTANCE;
		properties.setShouldCreateNewChat(shouldCreate);
		return properties;
	}

	@Override
	public boolean isAssignable(final ChatProperties chatProperties, final ChatProperties properties) {
		return chatProperties.getUri().equalsNoResource(properties.getUri());
	}

}
