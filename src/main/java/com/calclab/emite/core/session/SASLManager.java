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

package com.calclab.emite.core.session;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nullable;

import com.calclab.emite.base.util.Base64Utils;
import com.calclab.emite.base.xml.XMLBuilder;
import com.calclab.emite.base.xml.XMLPacket;
import com.calclab.emite.core.XmppNamespaces;
import com.calclab.emite.core.XmppURI;
import com.calclab.emite.core.conn.XmppConnection;
import com.calclab.emite.core.events.AuthorizationResultEvent;
import com.calclab.emite.core.events.PacketReceivedEvent;
import com.calclab.emite.core.sasl.Credentials;
import com.google.common.base.Ascii;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;

final class SASLManager implements PacketReceivedEvent.Handler {

	private final EventBus eventBus;
	private final XmppConnection connection;

	@Nullable private HandlerRegistration handler;
	@Nullable private Credentials currentCredentials;

	protected SASLManager(final EventBus eventBus, final XmppConnection connection) {
		this.eventBus = checkNotNull(eventBus);
		this.connection = checkNotNull(connection);
	}

	@Override
	public void onPacketReceived(final PacketReceivedEvent event) {
		final String name = event.getPacket().getTagName();
		if ("failure".equals(name)) { // & XMLNS
			eventBus.fireEventFromSource(new AuthorizationResultEvent(), this);
			currentCredentials = null;
		} else if ("success".equals(name)) {
			eventBus.fireEventFromSource(new AuthorizationResultEvent(currentCredentials), this);
			currentCredentials = null;
		}
	}

	/**
	 * Add a handler to know when an authorization transaction has a result
	 * 
	 * @param handler
	 */
	public HandlerRegistration addAuthorizationResultHandler(final AuthorizationResultEvent.Handler handler) {
		return eventBus.addHandlerToSource(AuthorizationResultEvent.TYPE, this, handler);
	}

	protected void sendAuthorizationRequest(final Credentials credentials) {
		if (handler == null) {
			handler = connection.addPacketReceivedHandler(this);
		}
		currentCredentials = credentials;
		final XMLPacket response = credentials.isAnoymous() ? createAnonymousAuthorization() : createPlainAuthorization(credentials);
		connection.send(response);
	}

	// TODO: Add DIGEST-MD5 auth
	// TODO: Add SCRAM-SHA1 auth

	private static XMLPacket createAnonymousAuthorization() {
		return XMLBuilder.create("auth", XmppNamespaces.SASL).attribute("mechanism", "ANONYMOUS").getXML();
	}

	private static XMLPacket createPlainAuthorization(final Credentials credentials) {
		final String encoded = encodePlain(credentials.getURI(), credentials.getPassword());
		return XMLBuilder.create("auth", XmppNamespaces.SASL).attribute("mechanism", "PLAIN").text(encoded).getXML();
	}

	private static String encodePlain(final XmppURI uri, final String password) {
		final String auth = uri.getJID().toString() + Ascii.NUL + uri.getNode() + Ascii.NUL + password;
		return Base64Utils.toBase64(auth.getBytes());
	}

}
