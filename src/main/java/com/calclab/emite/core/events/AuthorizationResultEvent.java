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

package com.calclab.emite.core.events;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nullable;

import com.calclab.emite.core.sasl.Credentials;
import com.google.web.bindery.event.shared.Event;

public class AuthorizationResultEvent extends Event<AuthorizationResultEvent.Handler> {

	public interface Handler {
		void onAuthorizationResult(AuthorizationResultEvent event);
	}

	public static final Type<Handler> TYPE = new Type<Handler>();

	@Nullable private final Credentials credentials;
	private final boolean success;

	/**
	 * Build a failed authorization event
	 */
	public AuthorizationResultEvent() {
		this(null, false);
	}

	/**
	 * Build a succeeded authorization event with the current credentials
	 * 
	 * @param uri
	 *            the uri of the authorized user
	 */
	public AuthorizationResultEvent(final Credentials credentials) {
		this(checkNotNull(credentials), true);
	}

	private AuthorizationResultEvent(@Nullable final Credentials credentials, final boolean success) {
		this.credentials = credentials;
		this.success = success;
	}

	@Nullable
	public Credentials getCredentials() {
		return credentials;
	}

	public boolean isSuccess() {
		return success;
	}

	@Override
	public Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	public String toDebugString() {
		final String value = success ? " Success" : " Failed!";
		return super.toDebugString() + value;
	}

	@Override
	protected void dispatch(final Handler handler) {
		handler.onAuthorizationResult(this);
	}

}
