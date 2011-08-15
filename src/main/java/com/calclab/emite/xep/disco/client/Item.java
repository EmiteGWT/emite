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

import com.calclab.emite.core.client.packet.IPacket;

/**
 * Each <item/> element MUST possess 'jid' attribute and MAY possess:
 * <ul>
 * <li>a 'name' attribute specifying a natural-language name for the entity;</li>
 * <li>a 'node' attribute specifying a hierarchical structure.</li>
 * </ul>
 * 
 * 
 */
public class Item {
	public static Item fromPacket(final IPacket packet) {
		return new Item(packet.getAttribute("jid"), packet.getAttribute("name"), packet.getAttribute("node"));
	}

	public final String jid;
	public final String name;
	public final String node;

	public Item(final String jid, final String name, final String node) {
		this.jid = jid;
		this.name = name;
		this.node = node;
	}
}
