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

package com.calclab.emite.core.client.xml;

public final class XMLBuilder implements HasXML {
	
	public static final XMLPacket fromXML(final String xml) {
		return XMLPacketImplGWT.fromString(xml);
	}
	
	public static final XMLBuilder create(final String name) {
		return new XMLBuilder(new XMLPacketImplGWT(name));
	}
	
	public static final XMLBuilder create(final String name, final String namespace) {
		return new XMLBuilder(new XMLPacketImplGWT(name, namespace));
	}
	
	private final XMLPacket xml;
	
	private XMLBuilder(final XMLPacket xml) {
		this.xml = xml;
	}
	
	public XMLBuilder attribute(final String name, final String value) {
		xml.setAttribute(name, value);
		return this;
	}
	
	public XMLBuilder child(final String name) {
		return new XMLBuilder(xml.addChild(name));
	}
	
	public XMLBuilder child(final String name, final String namespace) {
		return new XMLBuilder(xml.addChild(name, namespace));
	}
	
	public XMLBuilder childText(final String name, final String text) {
		xml.setChildText(name, text);
		return this;
	}
	
	public XMLBuilder text(final String text) {
		xml.setText(text);
		return this;
	}
	
	public XMLBuilder parent() {
		final XMLPacket parent = xml.getParent();
		return parent != null ? new XMLBuilder(parent) : this;
	}
	
	public XMLPacket getXML() {
		return xml.getFirstParent();
	}
	
	@Override
	public String toString() {
		return getXML().toString();
	}
	
}
