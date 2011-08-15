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

import com.calclab.emite.core.client.conn.StanzaEvent;
import com.calclab.emite.core.client.conn.StanzaHandler;
import com.calclab.emite.core.client.conn.XmppConnection;
import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.Packet;
import com.calclab.emite.core.client.xmpp.session.Credentials;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class SASLManager {
	private static final String SEP = new String(new char[] { 0 });
	private static final String XMLNS = "urn:ietf:params:xml:ns:xmpp-sasl";

	private final XmppConnection connection;
	private final DecoderRegistry decoders;
	private final EmiteEventBus eventBus;
	private Credentials currentCredentials;

	@Inject
	public SASLManager(final XmppConnection connection, final DecoderRegistry decoders) {
		this.connection = connection;
		eventBus = connection.getEventBus();
		this.decoders = decoders;

		connection.addStanzaReceivedHandler(new StanzaHandler() {
			@Override
			public void onStanza(final StanzaEvent event) {
				final IPacket stanza = event.getStanza();
				final String name = stanza.getName();
				if ("failure".equals(name)) { // & XMLNS
					eventBus.fireEvent(new AuthorizationResultEvent());
				} else if ("success".equals(name)) {
					eventBus.fireEvent(new AuthorizationResultEvent(currentCredentials));
				}
				currentCredentials = null;
			}
		});
	}

	/**
	 * Add a handler to know when an authorization transaction has a result
	 * 
	 * @param handler
	 */
	public void addAuthorizationResultHandler(final AuthorizationResultHandler handler) {
		AuthorizationResultEvent.bind(eventBus, handler);
	}

	public void sendAuthorizationRequest(final Credentials credentials) {
		currentCredentials = credentials;
		final IPacket response = credentials.isAnoymous() ? createAnonymousAuthorization() : createPlainAuthorization(credentials);
		connection.send(response);
	}

	private IPacket createAnonymousAuthorization() {
		final IPacket auth = new Packet("auth", XMLNS).With("mechanism", "ANONYMOUS");
		return auth;
	}

	private IPacket createPlainAuthorization(final Credentials credentials) {
		final IPacket auth = new Packet("auth", XMLNS).With("mechanism", "PLAIN");

		final PasswordDecoder decoder = decoders.getDecoder(credentials.getEncodingMethod());

		if (decoder == null)
			throw new RuntimeException("No password decoder found to convert from " + credentials.getEncodingMethod() + "to " + Credentials.ENCODING_BASE64);

		final String decodedPassword = decoder.decode(credentials.getEncodingMethod(), credentials.getEncodedPassword());
		final String encoded = encodeForPlainMethod(credentials.getXmppUri().getHost(), credentials.getXmppUri().getNode(), decodedPassword);
		auth.setText(encoded);
		return auth;
	}

	private String encodeForPlainMethod(final String domain, final String userName, final String password) {
		final String auth = userName + "@" + domain + SEP + userName + SEP + password;
		return Base64Coder.encodeString(auth);
	}

}
