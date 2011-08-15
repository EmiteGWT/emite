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

package com.calclab.emite.core.client.xmpp.stanzas;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;

import com.calclab.emite.core.client.packet.DelegatedPacket;
import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.Packet;

public class BasicStanza extends DelegatedPacket implements Stanza {
	protected static final String TYPE = "type";
	private static final String FROM = "from";
	private static final String ID = "id";
	private static final String TO = "to";

	public BasicStanza(final IPacket stanza) {
		super(stanza);
	}

	public BasicStanza(final String name, final String xmlns) {
		super(new Packet(name, xmlns));
	}

	@Override
	public XmppURI getFrom() {
		return uri(getAttribute(FROM));
	}

	@Override
	public String getFromAsString() {
		return getAttribute(FROM);
	}

	public String getId() {
		return getAttribute(ID);
	}

	@Override
	public XmppURI getTo() {
		return uri(getToAsString());
	}

	@Override
	public String getToAsString() {
		return getAttribute(TO);
	}

	@Override
	public void setFrom(final XmppURI from) {
		setAttribute(FROM, (from != null ? from.toString() : null));
	}

	public void setId(final String id) {
		setAttribute(ID, id);
	}

	@Override
	public void setTo(final XmppURI to) {
		setAttribute(TO, (to != null ? to.toString() : null));
	}

	public void setType(final String type) {
		setAttribute(TYPE, type);
	}
}
