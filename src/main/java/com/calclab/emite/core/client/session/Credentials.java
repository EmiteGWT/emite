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

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nullable;

import com.calclab.emite.core.client.uri.XmppURI;

public class Credentials {

	/**
	 * The URI required to perform an anonymous login
	 */
	private static final XmppURI ANONYMOUS = XmppURI.uri("anonymous", "", null);

	public static Credentials createAnonymous() {
		return new Credentials(ANONYMOUS, null);
	}

	private final XmppURI uri;
	@Nullable private final String password;

	public Credentials(final XmppURI uri, final String password) {
		if (checkNotNull(uri).getResource() == null) {
			this.uri = XmppURI.uri(uri.getNode(), uri.getHost(), "emite-" + System.currentTimeMillis());
		} else {
			this.uri = uri;
		}
		this.password = password;
	}

	public final XmppURI getUri() {
		return uri;
	}

	@Nullable
	public final String getPassword() {
		return password;
	}

	public boolean isAnoymous() {
		return uri.equalsNoResource(ANONYMOUS) && password == null;
	}
}
