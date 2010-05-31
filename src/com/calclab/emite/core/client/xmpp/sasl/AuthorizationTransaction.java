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

import com.calclab.emite.core.client.xmpp.session.Credentials;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;

public class AuthorizationTransaction {
    public static enum State {
	succeed, failed, notStarted, waitingForAuthorization
    }

    private State state;
    private final Credentials credentials;

    public AuthorizationTransaction(final Credentials credentials) {
	this.credentials = credentials;
    }

    public Credentials getCredentials() {
	return credentials;
    }

    public State getState() {
	return state;
    }

    public XmppURI getXmppUri() {
	return credentials.getXmppUri();
    }

    public void setState(final State state) {
	this.state = state;
    }

}
