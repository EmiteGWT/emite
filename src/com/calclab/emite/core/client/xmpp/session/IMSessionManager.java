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
package com.calclab.emite.core.client.xmpp.session;

import com.calclab.emite.core.client.conn.Connection;
import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.xmpp.stanzas.IQ;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.suco.client.events.Event;
import com.calclab.suco.client.events.Listener;

/**
 * Handle the IM session request.
 * 
 * @see http://www.xmpp.org/extensions/xep-0206.html#preconditions-sasl
 */
public class IMSessionManager {
    private final Connection connection;
    private final Event<XmppURI> onSessionCreated;

    public IMSessionManager(final Connection connection) {
	this.connection = connection;
	this.onSessionCreated = new Event<XmppURI>("sessionManager:onSessionCreated");

	connection.onStanzaReceived(new Listener<IPacket>() {
	    public void onEvent(final IPacket stanza) {
		if ("im-session-request".equals(stanza.getAttribute("id"))) {
		    onSessionCreated.fire(XmppURI.uri(stanza.getAttribute("to")));
		}
	    }

	});
    }

    public void onSessionCreated(final Listener<XmppURI> listener) {
	onSessionCreated.add(listener);
    }

    public void requestSession(final XmppURI uri) {
	final IQ iq = new IQ(IQ.Type.set, uri.getHostURI());
	iq.setFrom(uri);
	iq.setAttribute("id", "im-session-request");
	iq.Includes("session", "urn:ietf:params:xml:ns:xmpp-session");

	connection.send(iq);
    }
}
