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

package com.calclab.emite.xtesting.services;

import java.util.Queue;

import tigase.xml.DomBuilderHandler;
import tigase.xml.Element;
import tigase.xml.SimpleParser;

import com.calclab.emite.core.client.packet.IPacket;

public class TigaseXMLService {
	public static final TigaseXMLService instance = new TigaseXMLService();

	public static final IPacket toPacket(final String xml) {
		return instance.toXML(xml);
	}

	private final SimpleParser parser;

	public TigaseXMLService() {
		parser = new SimpleParser();
	}

	public String toString(final IPacket iPacket) {
		return iPacket != null ? iPacket.toString() : "null";
	}

	public IPacket toXML(final String xml) {
		final DomBuilderHandler handler = new DomBuilderHandler();
		parser.parse(handler, xml.toCharArray(), 0, xml.length());
		final Queue<Element> parsedElements = handler.getParsedElements();

		final Element body = parsedElements.poll();
		if (body == null)
			throw new RuntimeException("not valid xml: " + xml);
		return new TigasePacket(body);
	}
}
