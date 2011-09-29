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

package com.calclab.emite.core.client.session;

import com.calclab.emite.core.client.stanzas.XmppURI;

public class Credentials {

	/**
	 * The URI required to perform an anonymous login
	 */
	private static final XmppURI ANONYMOUS = XmppURI.uri("anonymous", "", null);

	public static Credentials createAnonymous() {
		return new Credentials(ANONYMOUS, null);
	}

	private final XmppURI uri;
	private final String password;

	public Credentials(final XmppURI uri, final String password) {
		if (uri == null)
			throw new NullPointerException("uri can't be null in LoginCredentials");

		if (uri.getResource() == null) {
			this.uri = XmppURI.uri(uri.getNode(), uri.getHost(), "emite-" + System.currentTimeMillis());
		} else {
			this.uri = uri;
		}
		this.password = password;
	}

	public final XmppURI getXmppUri() {
		return uri;
	}

	public final String getPassword() {
		return password;
	}

	public boolean isAnoymous() {
		return ANONYMOUS.getNode().equals(uri.getNode()) && ANONYMOUS.getHost().equals(uri.getHost());
	}
}
