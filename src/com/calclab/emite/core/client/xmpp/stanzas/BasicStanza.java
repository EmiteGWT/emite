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

    public XmppURI getFrom() {
        return uri(getAttribute(FROM));
    }

    public String getFromAsString() {
        return getAttribute(FROM);
    }

    public String getId() {
        return getAttribute(ID);
    }

    public XmppURI getTo() {
        return uri(getToAsString());
    }

    public String getToAsString() {
        return getAttribute(TO);
    }

    public void setFrom(final XmppURI from) {
        setAttribute(FROM, (from != null ? from.toString() : null));
    }

    public void setId(final String id) {
        setAttribute(ID, id);
    }

    public void setTo(final XmppURI to) {
        setAttribute(TO, (to != null ? to.toString() : null));
    }

    public void setType(final String type) {
        setAttribute(TYPE, type);
    }
}
