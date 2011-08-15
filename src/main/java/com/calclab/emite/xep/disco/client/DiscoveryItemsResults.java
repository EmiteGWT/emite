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

package com.calclab.emite.xep.disco.client;

import java.util.ArrayList;
import java.util.List;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.MatcherFactory;
import com.calclab.emite.core.client.packet.PacketMatcher;
import com.calclab.emite.core.client.xmpp.stanzas.IQ;

/**
 * Discovery items result object.
 * 
 * It contains zero or more Item objects
 * 
 * @see http://xmpp.org/extensions/xep-0030.html#items
 */
public class DiscoveryItemsResults {
	public static final PacketMatcher ITEMS_RESULT_MATCHER = MatcherFactory.byNameAndXMLNS("query", "http://jabber.org/protocol/disco#items");
	public static final PacketMatcher ITEMS_MATCHER = MatcherFactory.byName("item");
	private List<Item> items;
	private final IPacket result;

	public DiscoveryItemsResults(final IQ iq) {
		assert IQ.Type.result == iq.getType();
		result = iq.getFirstChild(ITEMS_RESULT_MATCHER);

	}

	public List<Item> getItems() {
		if (items == null) {
			items = processItem(result.getChildren(ITEMS_MATCHER));
		}
		return items;
	}

	private List<Item> processItem(final List<? extends IPacket> children) {
		final List<Item> items = new ArrayList<Item>();
		for (final IPacket child : children) {
			items.add(Item.fromPacket(child));
		}
		return items;
	}
}
