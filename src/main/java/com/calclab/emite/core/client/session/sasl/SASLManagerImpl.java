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

import com.calclab.emite.core.client.conn.XmppConnection;
import com.calclab.emite.core.client.events.AuthorizationResultEvent;
import com.calclab.emite.core.client.events.StanzaReceivedEvent;
import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.Packet;
import com.calclab.emite.core.client.session.Credentials;
import com.calclab.emite.core.client.util.Base64Utils;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;

@Singleton
public class SASLManagerImpl implements SASLManager, StanzaReceivedEvent.Handler {
	
	private static final String SEP = new String(new char[] { 0 });
	private static final String XMLNS = "urn:ietf:params:xml:ns:xmpp-sasl";

	private final EventBus eventBus;
	private final XmppConnection connection;
	
	private Credentials currentCredentials;

	@Inject
	public SASLManagerImpl(@Named("emite") final EventBus eventBus, final XmppConnection connection) {
		this.eventBus = eventBus;
		this.connection = connection;

		connection.addStanzaReceivedHandler(this);
	}
	
	@Override
	public void onStanzaReceived(final StanzaReceivedEvent event) {
		final IPacket stanza = event.getStanza();
		final String name = stanza.getName();
		if ("failure".equals(name)) { // & XMLNS
			eventBus.fireEventFromSource(new AuthorizationResultEvent(), this);
		} else if ("success".equals(name)) {
			eventBus.fireEventFromSource(new AuthorizationResultEvent(currentCredentials), this);
		}
		currentCredentials = null;
	}

	/**
	 * Add a handler to know when an authorization transaction has a result
	 * 
	 * @param handler
	 */
	public HandlerRegistration addAuthorizationResultHandler(final AuthorizationResultEvent.Handler handler) {
		return eventBus.addHandlerToSource(AuthorizationResultEvent.TYPE, this, handler);
	}

	public void sendAuthorizationRequest(final Credentials credentials) {
		currentCredentials = credentials;
		final IPacket response = credentials.isAnoymous() ? createAnonymousAuthorization() : createPlainAuthorization(credentials);
		connection.send(response);
	}
	
	// TODO: Add DIGEST-MD5 auth

	private IPacket createAnonymousAuthorization() {
		final IPacket auth = new Packet("auth", XMLNS).With("mechanism", "ANONYMOUS");
		return auth;
	}

	private IPacket createPlainAuthorization(final Credentials credentials) {
		final IPacket auth = new Packet("auth", XMLNS).With("mechanism", "PLAIN");

		final String encoded = encodeForPlainMethod(credentials.getXmppUri().getHost(), credentials.getXmppUri().getNode(), credentials.getPassword());
		auth.setText(encoded);
		return auth;
	}

	private String encodeForPlainMethod(final String domain, final String userName, final String password) {
		final String auth = userName + "@" + domain + SEP + userName + SEP + password;
		return Base64Utils.toBase64(auth.getBytes());
	}

}
