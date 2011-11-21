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

import static com.google.common.base.Preconditions.checkNotNull;
import static com.calclab.emite.core.XmppURI.uri;

import javax.annotation.Nullable;

import com.calclab.emite.base.xml.HasXML;
import com.calclab.emite.base.xml.XMLBuilder;
import com.calclab.emite.base.xml.XMLPacket;
import com.calclab.emite.core.XmppURI;

/**
 * Represents an XMPP Stanza.
 * 
 * Basic stanzas have <i>id</i>, <i>from</i> and <i>to</i> attributes,
 * as well as support for XML extensions.
 */
public abstract class Stanza implements HasXML {

	/**
	 * Holds the XML packet associated with this stanza.
	 */
	protected final XMLPacket xml;

	/**
	 * Creates a new stanza from an XML packet.
	 * 
	 * No checks are done to the packet, so it's only meant for internal use.
	 * 
	 * @param xml the XML packet for this stanza
	 */
	protected Stanza(final XMLPacket xml) {
		this.xml = checkNotNull(xml);
	}

	/**
	 * Create a new stanza with the given tag name.
	 * 
	 * @param name the tag name for this stanza
	 */
	protected Stanza(final String name) {
		this(XMLBuilder.create(name).getXML());
	}

	/**
	 * Creates a new stanza with the given tag name and namespace.
	 * 
	 * @param name the tag name for this stanza
	 * @param namespace the namespace for this stanza
	 */
	protected Stanza(final String name, final String namespace) {
		this(XMLBuilder.create(name, namespace).getXML());
	}

	/**
	 * Returns the <i>id</i> attribute for this stanza.
	 * 
	 * @return the ID for this stanza, or {@code null} if none
	 */
	@Nullable
	public final String getId() {
		return xml.getAttribute("id");
	}

	/**
	 * Sets a new <i>id</i> attribute for this stanza.
	 * 
	 * @param id the new ID for this stanza
	 */
	public final void setId(@Nullable final String id) {
		xml.setAttribute("id", id);
	}

	/**
	 * Returns the <i>from</i> attribute for this stanza.
	 * 
	 * @return the sender for this stanza, or {@code null} if none
	 */
	@Nullable
	public final XmppURI getFrom() {
		return uri(xml.getAttribute("from"));
	}

	/**
	 * Sets a new <i>from</i> attribute for this stanza.
	 * 
	 * @param from the new sender for this stanza
	 */
	public final void setFrom(@Nullable final XmppURI from) {
		xml.setAttribute("from", from != null ? from.toString() : null);
	}

	/**
	 * Returns the <i>to</i> attribute for this stanza.
	 * 
	 * @return the recipient for this stanza, or {@code null} if none
	 */
	@Nullable
	public final XmppURI getTo() {
		return uri(xml.getAttribute("to"));
	}

	/**
	 * Sets a new <i>to</i> attribute for this stanza.
	 * 
	 * @param to the new recipient for this stanza
	 */
	public final void setTo(@Nullable final XmppURI to) {
		xml.setAttribute("to", to != null ? to.toString() : null);
	}
	
	/**
	 * Retrieves a XML extension from this packet.
	 * 
	 * @param name the name of the extension
	 * @param namespace the namespace of the extension
	 * @return the XML extension, or {@code null} if not found
	 */
	@Nullable
	public final XMLPacket getExtension(final String name, final String namespace) {
		return xml.getFirstChild(name, namespace);
	}
	
	/**
	 * Adds a new XML extension to this packet.
	 * 
	 * @param name the name of the extension
	 * @param namespace the namespace of the extension
	 * @return the new XML extension
	 */
	public final XMLPacket addExtension(final String name, final String namespace) {
		return xml.addChild(name, namespace);
	}
	
	@Override
	public final XMLPacket getXML() {
		return xml;
	}

	@Override
	public final String toString() {
		return xml.toString();
	}

}
