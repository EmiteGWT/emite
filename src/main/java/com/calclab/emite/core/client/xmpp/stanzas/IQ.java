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

import com.calclab.emite.core.client.packet.IPacket;

public class IQ extends BasicStanza {
    public static enum Type {
	error, get, result, set
    }

    private static final String NAME = "iq";

    public static Type getType(IPacket packet) {
	try {
	    return Type.valueOf(packet.getAttribute(TYPE));
	} catch (final IllegalArgumentException e) {
	    return null;
	}
    }

    /**
     * Use isType
     */
    @Deprecated
    public static boolean isSet(final IPacket iq) {
	return iq.hasAttribute(TYPE, "set");
    }

    public static boolean isSuccess(final IPacket iq) {
	return iq.hasAttribute(TYPE, "result");
    }

    public static boolean isType(Type type, final IPacket iq) {
	return iq.hasAttribute(TYPE, type.toString());
    }

    public IQ(final IPacket stanza) {
	super(stanza);
    }

    public IQ(final Type type) {
	super(NAME, null);
	if (type != null) {
	    setType(type.toString());
	}
    }

    /**
     * Create a new IQ
     * 
     * @param type
     *            type of the IQ
     * @param to
     *            iq recipient
     */
    public IQ(final Type type, final XmppURI to) {
	this(type);
	super.setTo(to);
    }

    public IPacket addQuery(final String xmlns) {
	final IPacket query = addChild("query", xmlns);
	return query;
    }

    public IQ From(final XmppURI fromURI) {
	setFrom(fromURI);
	return this;
    }

    public Type getType() {
	return getType(this);
    }

    public IPacket Includes(final String name, final String xmlns) {
	addChild(name, xmlns);
	return this;
    }

    public boolean isType(Type type) {
	return IQ.isType(type, this);
    }

    public IQ To(final XmppURI toURI) {
	setTo(toURI);
	return this;
    }

    @Override
    public IQ With(String name, String value) {
	return (IQ) super.With(name, value);
    }

    public IQ WithQuery(final String xmlns) {
	addQuery(xmlns);
	return this;
    }

}
