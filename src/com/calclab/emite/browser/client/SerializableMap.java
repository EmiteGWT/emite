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
package com.calclab.emite.browser.client;

import java.util.HashMap;

/**
 * An extension to provide serialize/restore from HashMap to String in order to
 * store information in the browser's cookies.
 */
@SuppressWarnings("serial")
public class SerializableMap extends HashMap<String, String> {

    public static SerializableMap restore(final String serialized) {
	final SerializableMap map = new SerializableMap();
	final int total = serialized.length() - 1;
	String key, value;
	int begin, end, next;
	next = -1;

	do {
	    begin = next + 1;
	    end = serialized.indexOf('#', begin + 1);
	    next = serialized.indexOf('#', end + 1);
	    key = serialized.substring(begin, end);
	    value = serialized.substring(end + 1, next);
	    map.put(key, value);
	} while (next < total);

	return map;
    }

    public String serialize() {
	final StringBuilder builder = new StringBuilder();
	for (final String key : keySet()) {
	    builder.append(key).append("#").append(get(key)).append("#");
	}
	return builder.toString();
    }

}
