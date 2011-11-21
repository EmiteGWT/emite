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

package com.calclab.emite.base.xml;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nullable;

/**
 * Helper class to build and parse XML packets.
 */
public final class XMLBuilder implements HasXML {
	
	/**
	 * Parses a XML string into a {@link XMLPacket}.
	 * 
	 * @param xml the String to be parsed
	 * @return the parsed XMLPacket, or null if there was an error
	 */
	@Nullable
	public static final XMLPacket fromXML(final String xml) {
		return XMLPacketImplGWT.fromString(xml);
	}
	
	/**
	 * Creates a new XMLPacket with a given tag name.
	 * 
	 * @param name the tag name for the new XML element
	 * @return the new XMLPacket
	 */
	public static final XMLBuilder create(final String name) {
		return new XMLBuilder(new XMLPacketImplGWT(name));
	}
	
	/**
	 * Creates a new XMLPacket with a given tag name and namespace.
	 * 
	 * @param name the tag name for the new XML element
	 * @param namespace the namespace for the new XML element
	 * @return the new XMLPacket
	 */
	public static final XMLBuilder create(final String name, @Nullable final String namespace) {
		return new XMLBuilder(new XMLPacketImplGWT(name, namespace));
	}
	
	private final XMLPacket xml;
	
	private XMLBuilder(final XMLPacket xml) {
		this.xml = checkNotNull(xml);
	}
	
	/**
	 * Adds a new attribute to the current element.
	 * 
	 * @param name the attribute name
	 * @param value the attribute value
	 * @return the same XMLBuilder
	 */
	public final XMLBuilder attribute(final String name, final String value) {
		xml.setAttribute(name, value);
		return this;
	}
	
	/**
	 * Adds a new child to the current element.
	 * 
	 * Note: unlike other child() methods, this one returns the same XMBLuider.
	 * 
	 * @param child the child to be added
	 * @return the same XMLBuilder
	 */
	public final XMLBuilder child(final HasXML child) {
		xml.addChild(child);
		return this;
	}
	
	/**
	 * Adds a new child with the given name.
	 * 
	 * @param name the tag name of the child to be added
	 * @return a new XMLBuilder for the child
	 */
	public final XMLBuilder child(final String name) {
		return new XMLBuilder(xml.addChild(name));
	}
	
	/**
	 * Adds a new child with the given name and namespace.
	 * 
	 * @param name the tag name for the child to be added
	 * @param namespace the namespace for the child to be added
	 * @return a new XMLBuilder for the child
	 */
	public final XMLBuilder child(final String name, final String namespace) {
		return new XMLBuilder(xml.addChild(name, namespace));
	}
	
	/**
	 * Adds a new child with the given text.
	 * 
	 * @param name the tag name for the child to be added
	 * @param text the text contents for the child to be added
	 * @return the same XMLBuilder
	 */
	public final XMLBuilder childText(final String name, final String text) {
		xml.setChildText(name, text);
		return this;
	}
	
	/**
	 * Sets the text contents of the current element.
	 * 
	 * @param text the text content to be set
	 * @return the same XMLBuilder
	 */
	public final XMLBuilder text(final String text) {
		xml.setText(text);
		return this;
	}
	
	/**
	 * Returns the parent builder.
	 * 
	 * @return the builder for this element's parent
	 */
	public final XMLBuilder parent() {
		final XMLPacket parent = xml.getParent();
		return parent != null ? new XMLBuilder(parent) : this;
	}
	
	public final XMLPacket getXML() {
		return xml.getFirstParent();
	}
	
	@Override
	public final String toString() {
		return getXML().toString();
	}
	
}
