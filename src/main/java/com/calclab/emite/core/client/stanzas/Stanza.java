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

import static com.calclab.emite.core.client.stanzas.XmppURI.uri;

import com.calclab.emite.core.client.xml.HasXML;
import com.calclab.emite.core.client.xml.XMLBuilder;
import com.calclab.emite.core.client.xml.XMLPacket;

public class Stanza implements HasXML {

	protected final XMLPacket xml;

	public Stanza(final XMLPacket xml) {
		this.xml = xml;
	}

	public Stanza(final String name) {
		this(XMLBuilder.create(name).getXML());
	}

	public Stanza(final String name, final String namespace) {
		this(XMLBuilder.create(name, namespace).getXML());
	}

	public String getId() {
		return xml.getAttribute("id");
	}

	public void setId(final String id) {
		xml.setAttribute("id", id);
	}

	public XmppURI getFrom() {
		return uri(xml.getAttribute("from"));
	}

	public void setFrom(final XmppURI from) {
		xml.setAttribute("from", from != null ? from.toString() : null);
	}

	public XmppURI getTo() {
		return uri(xml.getAttribute("to"));
	}

	public void setTo(final XmppURI to) {
		xml.setAttribute("to", to != null ? to.toString() : null);
	}

	@Override
	public final XMLPacket getXML() {
		return xml;
	}
	
	@Override
	public String toString() {
		return xml.toString();
	}

}
