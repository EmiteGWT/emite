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

package com.calclab.emite.core.stanzas;

import javax.annotation.Nullable;

import com.calclab.emite.base.xml.XMLPacket;

/**
 * An Info/Query stanza.
 * 
 * @see <a href="http://xmpp.org/rfcs/rfc6120.html#stanzas-semantics-iq">RFC 6120 - Section 8.2.3</a>
 */
public class IQ extends Stanza {

	/**
	 * Possible <i>type</i> values for IQs.
	 * 
	 * @see <a href="http://xmpp.org/rfcs/rfc6120.html#stanzas-semantics-iq">RFC 6120 - Section 8.2.3</a>
	 */
	public static enum Type {
		get, set, result, error;
	}

	/**
	 * Creates a new IQ from a XML packet.
	 * 
	 * No checks are done to the packet, so it's only meant for internal use.
	 * 
	 * @param xml the XML packet for this IQ
	 */
	public IQ(final XMLPacket xml) {
		super(xml);
	}

	/**
	 * Create a new IQ with the given type.
	 * 
	 * @param type the type for the new IQ
	 */
	public IQ(final Type type) {
		super("iq");
		setType(type);
	}

	/**
	 * Returns the <i>type</i> attribute for this IQ.
	 * 
	 * @return the type for this stanza, or {@code null} if not found
	 */
	@Nullable
	public final Type getType() {
		try {
			return Type.valueOf(xml.getAttribute("type"));
		} catch (final IllegalArgumentException e) {
			return null;
		}
	}

	/**
	 * Sets a new <i>type</i> attribute for this IQ.
	 * 
	 * @param type the new type for this IQ
	 */
	public final void setType(final Type type) {
		xml.setAttribute("type", type.toString());
	}

	/**
	 * Retrieves the query from this IQ.
	 * 
	 * @param namespace the namespace of the query
	 * @return the query, or {@code null} if not found
	 */
	@Nullable
	public final XMLPacket getQuery(final String namespace) {
		return getExtension("query", namespace);
	}
	
	/**
	 * Adds a new query to this packet.
	 * 
	 * @param namespace the namespace of the query
	 * @return the new query
	 */
	public final XMLPacket addQuery(final String namespace) {
		return addExtension("query", namespace);
	}
}
