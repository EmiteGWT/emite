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

import com.google.gwt.xml.client.XMLParser;
import com.google.gwt.xml.client.impl.DOMParseException;

public class XMLUtils {

	public static final XMLPacket createPacket(final String name) {
		return new XMLPacketImplGWT(name);
	}

	public static final XMLPacket createPacket(final String name, final String namespace) {
		return new XMLPacketImplGWT(name, namespace);
	}

	public static final XMLPacket fromXML(final String xml) {
		try {
			return new XMLPacketImplGWT(XMLParser.parse(xml).getDocumentElement());
		} catch (final DOMParseException e) {
			return null;
		}
	}

}
