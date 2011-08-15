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

package com.calclab.emite.core.client.xmpp.sasl;

import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.core.client.xmpp.session.Credentials;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class AuthorizationResultEvent extends GwtEvent<AuthorizationResultHandler> {
	private static final Type<AuthorizationResultHandler> TYPE = new Type<AuthorizationResultHandler>();

	public static HandlerRegistration bind(final EmiteEventBus eventBus, final AuthorizationResultHandler handler) {
		return eventBus.addHandler(TYPE, handler);
	}

	private final boolean succeed;

	private final Credentials credentials;

	/**
	 * Build a failed authorization event
	 */
	public AuthorizationResultEvent() {
		this(false, null);
	}

	/**
	 * Build a succeeded authorization event with the current credentials
	 * 
	 * @param uri
	 *            the uri of the authorized user
	 */
	public AuthorizationResultEvent(final Credentials credentials) {
		this(true, credentials);
	}

	private AuthorizationResultEvent(final boolean succeed, final Credentials credentials) {
		this.succeed = succeed;
		this.credentials = credentials;
	}

	@Override
	public Type<AuthorizationResultHandler> getAssociatedType() {
		return TYPE;
	}

	public Credentials getCredentials() {
		return credentials;
	}

	public XmppURI getXmppUri() {
		return credentials.getXmppUri();
	}

	public boolean isSucceed() {
		return succeed;
	}

	@Override
	public String toDebugString() {
		final String value = succeed ? " Succeed - " + credentials.getXmppUri() : " Failed!";
		return super.toDebugString() + value;
	}

	@Override
	protected void dispatch(final AuthorizationResultHandler handler) {
		handler.onAuthorization(this);
	}

}
