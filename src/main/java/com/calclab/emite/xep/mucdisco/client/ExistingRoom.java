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

package com.calclab.emite.xep.mucdisco.client;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;

/**
 * A existing room
 */
public class ExistingRoom implements Comparable<ExistingRoom> {
	private final String name;
	private final XmppURI uri;

	public ExistingRoom(final XmppURI uri, final String name) {
		assert uri != null : "URI can't be null in existing rooms";
		this.uri = uri;
		if (name == null) {
			this.name = uri.toString();
		} else {
			this.name = name;
		}
	}

	@Override
	public int compareTo(final ExistingRoom o) {
		return name.compareTo(o.name);
	}

	public String getName() {
		return name;
	}

	public XmppURI getUri() {
		return uri;
	}

}