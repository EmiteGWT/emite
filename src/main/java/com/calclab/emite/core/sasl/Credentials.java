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

package com.calclab.emite.core.sasl;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.calclab.emite.core.XmppURI;
import com.google.common.base.Objects;

/**
 * XMPP authentication credentials.
 */
@Immutable
public final class Credentials {

	/**
	 * Anonymous credentials.
	 */
	public static final Credentials ANONYMOUS = new Credentials();

	@Nullable private final XmppURI uri;
	@Nullable private final String password;

	/**
	 * Create anonymous credentials.
	 */
	private Credentials() {
		this.uri = null;
		this.password = null;
	}
	
	/**
	 * Create a new set of credentials with the provided URI and optional password.
	 * 
	 * @param uri the URI for these credentials
	 * @param password the password for these credentials
	 */
	public Credentials(@Nullable final XmppURI uri, @Nullable final String password) {
		this.uri = checkNotNull(uri);
		this.password = password;
	}

	/**
	 * Return the URI for these credentials.
	 * 
	 * @return the URI, {@code null} for anonymous credentials
	 */
	@Nullable
	public final XmppURI getURI() {
		return uri;
	}

	/**
	 * Return the password for these credentials.
	 * 
	 * @return the password, or {@code null} if no password is set.
	 */
	@Nullable
	public final String getPassword() {
		return password;
	}

	/**
	 * Check if this is an anonymous login.
	 * 
	 * @return {@code true} if anonymous, {@code false} otherwise
	 */
	public final boolean isAnoymous() {
		return uri == null;
	}
	
	@Override
	public final int hashCode() {
		return Objects.hashCode(uri, password);
	}
	
	@Override
	public final boolean equals(final Object obj) {
		if (obj instanceof Credentials) {
			final Credentials other = (Credentials) obj;
			
			return Objects.equal(uri, other.uri) && Objects.equal(password, other.password);
		}
		return false;
	}
	
	@Override
	public final String toString() {
		if (isAnoymous())
			return "Anonymous credentials";
		
		return "Credentials for " + uri.toString();
	}
	
}
