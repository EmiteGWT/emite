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
    private static final String PREFIX = "xmpp:";
    private static final int PREFIX_LENGTH = PREFIX.length();
    private final HashMap<String, XmppURI> cache = new HashMap<String, XmppURI>();

    public XmppURI parse(final String xmppUri) {
	if (xmppUri == null || xmppUri.length() == 0) {
	    return null;
	}

	final String uri = xmppUri.startsWith(PREFIX) ? xmppUri.substring(PREFIX_LENGTH).toLowerCase() : xmppUri
		.toLowerCase();
	final XmppURI cached = cache.get(uri);
	if (cached != null) {
	    return cached;
	}

	String node = null;
	String domain = null;
	String resource = null;

	final int atIndex = uri.indexOf('@') + 1;
	if (atIndex > 0) {
	    node = uri.substring(0, atIndex - 1);
	    if (node.length() == 0) {
		return null;
		// throw new RuntimeException("a uri with @ should have node");
	    }
	}

	final int barIndex = uri.indexOf('/', atIndex);
	if (atIndex == barIndex) {
	    return null;
	    // throw new RuntimeException("bad syntax!");
	}
	if (barIndex > 0) {
	    domain = uri.substring(atIndex, barIndex);
	    resource = uri.substring(barIndex + 1);
	} else {
	    domain = uri.substring(atIndex);
	}
	if (domain.length() == 0) {
	    return null;
	    // throw new RuntimeException("The domain is required");
	}

	return XmppURI.uri(node, domain, resource);
    }

    XmppURI cache(final XmppURI xmppURI) {
	cache.put(xmppURI.toString(), xmppURI);
	return xmppURI;
    }

}
