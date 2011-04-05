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

import java.util.HashMap;

public class XmppURIFactory {
    private final HashMap<String, XmppURI> cache = new HashMap<String, XmppURI>();

    public XmppURI parse(final String xmppUri) {
	if (xmppUri == null || xmppUri.length() == 0) {
	    return null;
	}

	final String uri = XmppUriParser.removePrefix(xmppUri);
	XmppURI cached = cache.get(uri);
	if (cached == null) {
	    cached = XmppUriParser.parse(uri);
	    cache.put(uri, cached);
	}
	return cached;
    }

    XmppURI cache(final XmppURI xmppURI) {
	if (xmppURI != null) {
	    cache.put(xmppURI.toString(), xmppURI);
	}
	return xmppURI;
    }

}
