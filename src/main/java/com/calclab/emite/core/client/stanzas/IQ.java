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

package com.calclab.emite.core.client.stanzas;

import com.calclab.emite.core.client.xml.XMLPacket;

public class IQ extends Stanza {

	public static enum Type {
		error, get, result, set
	}

	public static Type getType(final XMLPacket packet) {
		try {
			return Type.valueOf(packet.getAttribute("type"));
		} catch (final IllegalArgumentException e) {
			return null;
		}
	}

	public static boolean isSuccess(final XMLPacket iq) {
		return "result".equals(iq.getAttribute("type"));
	}

	public static boolean isType(final Type type, final XMLPacket iq) {
		return type.toString().equals(iq.getAttribute("type"));
	}

	public IQ(final XMLPacket stanza) {
		super(stanza);
	}

	public IQ(final Type type) {
		super("iq");
		setType(type);
	}

	public boolean isType(final Type type) {
		return type.equals(getType());
	}

	public Type getType() {
		final String type = xml.getAttribute("type");
		try {
			return type != null ? Type.valueOf(type) : null;
		} catch (final IllegalArgumentException e) {
			return null;
		}
	}

	public void setType(final Type type) {
		xml.setAttribute("type", type != null ? type.toString() : null);
	}

	public XMLPacket addChild(final String name, final String xmlns) {
		return xml.addChild(name, xmlns);
	}

	public XMLPacket getChild(final String name, final String xmlns) {
		return xml.getFirstChild(name, xmlns);
	}
}
