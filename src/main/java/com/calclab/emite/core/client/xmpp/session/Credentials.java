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

package com.calclab.emite.core.client.xmpp.session;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;

public class Credentials {
	/**
	 * The password is not encoded at all
	 */
	public static final String ENCODING_NONE = "none";
	/**
	 * The password is encoding using a Base64 algorithm
	 */
	public static final String ENCODING_BASE64 = "plain";

	/**
	 * The URI required to perform an anonymous login
	 */
	public static final XmppURI ANONYMOUS = XmppURI.uri("anonymous", "", null);

	public static Credentials createAnonymous() {
		return new Credentials(ANONYMOUS, null, ENCODING_NONE);
	}

	XmppURI uri;
	String encodedPassword;

	String encodingMethod;

	public Credentials(final XmppURI uri, final String encodedPassword, final String encodingMethod) {
		if (uri == null)
			throw new NullPointerException("uri can't be null in LoginCredentials");

		if (uri.getResource() == null) {
			this.uri = XmppURI.uri(uri.getNode(), uri.getHost(), "emite-" + System.currentTimeMillis());
		} else {
			this.uri = uri;
		}
		this.encodedPassword = encodedPassword;
		this.encodingMethod = encodingMethod;
	}

	public String getEncodedPassword() {
		return encodedPassword;
	}

	public String getEncodingMethod() {
		return encodingMethod;
	}

	public XmppURI getXmppUri() {
		return uri;
	}

	public boolean isAnoymous() {
		return ANONYMOUS.getNode().equals(uri.getNode()) && ANONYMOUS.getHost().equals(uri.getHost());
	}
}
