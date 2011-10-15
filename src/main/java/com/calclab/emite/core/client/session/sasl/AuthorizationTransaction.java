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

package com.calclab.emite.core.client.session.sasl;

import static com.google.common.base.Preconditions.checkNotNull;

import com.calclab.emite.core.client.session.Credentials;
import com.calclab.emite.core.client.uri.XmppURI;

public class AuthorizationTransaction {
	public static enum Status {
		succeed, failed, notStarted, waitingForAuthorization
	}

	private final Credentials credentials;
	private Status status;

	public AuthorizationTransaction(final Credentials credentials) {
		this.credentials = checkNotNull(credentials);
		status = Status.notStarted;
	}

	public Credentials getCredentials() {
		return credentials;
	}

	public Status getStatus() {
		return status;
	}

	public XmppURI getXmppUri() {
		return credentials.getUri();
	}

	public void setStatus(final Status status) {
		this.status = checkNotNull(status);
	}

}
