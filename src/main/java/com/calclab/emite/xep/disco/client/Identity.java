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
 * Each <identity/> element MUST possess 'category' and 'type' attributes
 * specifying the category and type for the entity, and MAY possess a 'name'
 * attribute specifying a natural-language name for the entity
 * 
 * 
 */
public class Identity {
	public static Identity fromPacket(final IPacket packet) {
		return new Identity(packet.getAttribute("category"), packet.getAttribute("type"), packet.getAttribute("name"));
	}

	public final String category;
	public final String type;
	public final String name;

	public Identity(final String category, final String type, final String name) {
		this.category = category;
		this.type = type;
		this.name = name;
	}
}
