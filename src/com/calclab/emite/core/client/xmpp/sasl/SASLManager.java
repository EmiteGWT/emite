/*
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008-2009 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.calclab.emite.core.client.xmpp.sasl;

import com.calclab.emite.core.client.bosh.Connection;
import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.Packet;
import com.calclab.emite.core.client.xmpp.sasl.AuthorizationTransaction.State;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.suco.client.events.Event;
import com.calclab.suco.client.events.Listener;

public class SASLManager {
    private static final String SEP = new String(new char[] { 0 });
    private static final String XMLNS = "urn:ietf:params:xml:ns:xmpp-sasl";
    public static final XmppURI ANONYMOUS = XmppURI.uri("anonymous", "", null);

    private final Event<AuthorizationTransaction> onAuthorized;
    private AuthorizationTransaction currentTransaction;
    private final Connection connection;

    public SASLManager(final Connection connection) {
	this.connection = connection;
	onAuthorized = new Event<AuthorizationTransaction>("saslManager:onAuthorized");
	install();
    }

    public void onAuthorized(final Listener<AuthorizationTransaction> listener) {
	onAuthorized.add(listener);
    }

    public void sendAuthorizationRequest(final AuthorizationTransaction authorizationTransaction) {
	currentTransaction = authorizationTransaction;
	final IPacket response = isAnonymous(authorizationTransaction) ? createAnonymousAuthorization()
		: createPlainAuthorization(authorizationTransaction);
	connection.send(response);
	currentTransaction.setState(State.waitingForAuthorization);
    }

    private IPacket createAnonymousAuthorization() {
	final IPacket auth = new Packet("auth", XMLNS).With("mechanism", "ANONYMOUS");
	return auth;
    }

    private IPacket createPlainAuthorization(final AuthorizationTransaction authorizationTransaction) {
	final IPacket auth = new Packet("auth", XMLNS).With("mechanism", "PLAIN");
	final String encoded = encode(authorizationTransaction.uri.getHost(), authorizationTransaction.uri.getNode(),
		authorizationTransaction.getPassword());
	auth.setText(encoded);
	return auth;
    }

    private String encode(final String domain, final String userName, final String password) {
	final String auth = userName + "@" + domain + SEP + userName + SEP + password;
	return Base64Coder.encodeString(auth);
    }

    private void install() {
	connection.onStanzaReceived(new Listener<IPacket>() {
	    public void onEvent(final IPacket stanza) {
		final String name = stanza.getName();
		if ("failure".equals(name)) { // & XMLNS
		    currentTransaction.setState(State.failed);
		    onAuthorized.fire(currentTransaction);
		    currentTransaction = null;
		} else if ("success".equals(name)) {
		    currentTransaction.setState(State.succeed);
		    onAuthorized.fire(currentTransaction);
		    currentTransaction = null;

		}
	    }
	});

    }

    private boolean isAnonymous(final AuthorizationTransaction authorizationTransaction) {
	return authorizationTransaction.uri == SASLManager.ANONYMOUS;
    }
}
