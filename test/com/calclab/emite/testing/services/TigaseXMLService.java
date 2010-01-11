/*
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008-2009 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.calclab.emite.testing.services;

import java.util.Queue;

import tigase.xml.DomBuilderHandler;
import tigase.xml.Element;
import tigase.xml.SimpleParser;

import com.calclab.emite.core.client.packet.IPacket;

public class TigaseXMLService {
    private static TigaseXMLService instance;

    public static TigaseXMLService getSingleton() {
	if (instance == null)
	    instance = new TigaseXMLService();
	return instance;
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
	if (body == null) {
	    throw new RuntimeException("not valid xml: " + xml);
	}
	return new TigasePacket(body);
    }
}
