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

package com.calclab.emite.xep.disco;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.calclab.emite.base.xml.HasXML;
import com.calclab.emite.base.xml.XMLBuilder;
import com.calclab.emite.base.xml.XMLPacket;
import com.calclab.emite.core.XmppURI;
import com.google.common.base.Objects;

/**
 * Each <item/> element MUST possess 'jid' attribute and MAY possess:
 * <ul>
 * <li>a 'name' attribute specifying a natural-language name for the entity;</li>
 * <li>a 'node' attribute specifying a hierarchical structure.</li>
 * </ul>
 */
@Immutable
public final class Item implements HasXML {

	private final XmppURI jid;
	@Nullable private final String name;
	@Nullable private final String node;

	protected Item(final XmppURI jid, @Nullable final String name, @Nullable final String node) {
		this.jid = checkNotNull(jid);
		this.name = name;
		this.node = node;
	}
	
	public final XmppURI getJID() {
		return jid;
	}
	
	@Nullable
	public final String getName() {
		return name;
	}
	
	@Nullable
	public final String getNode() {
		return node;
	}
	
	@Override
	public final int hashCode() {
		return Objects.hashCode(jid, name, node);
	}
	
	@Override
	public final boolean equals(final Object obj) {
		if (obj instanceof Item) {
			final Item other = (Item) obj;
			
			return jid.equals(other.jid) && Objects.equal(name, other.name) && Objects.equal(node, other.node);
		}
		
		return super.equals(obj);
	}
	
	@Override
	public final XMLPacket getXML() {
		return XMLBuilder.create("item").attribute("jid", jid.toString()).attribute("name", name).attribute("node", node).getXML();
	}
	
}
