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

import com.calclab.emite.core.client.conn.StanzaEvent;
import com.calclab.emite.core.client.conn.StanzaHandler;
import com.calclab.emite.core.client.conn.XmppConnection;
import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.xmpp.stanzas.IQ;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.suco.client.events.Listener;

/**
 * Handle the IM session request. Used by XmppSession (not need to be used by
 * clients)
 * 
 * @see http://www.xmpp.org/extensions/xep-0206.html#preconditions-sasl
 */
public class IMSessionManager {
    private final XmppConnection connection;

    public IMSessionManager(final XmppConnection connection) {
	this.connection = connection;

	connection.addStanzaReceivedHandler(new StanzaHandler() {
	    @Override
	    public void onStanza(final StanzaEvent event) {
		final IPacket stanza = event.getStanza();
		if ("im-session-request".equals(stanza.getAttribute("id"))) {
		    connection.getEventBus().fireEvent(
			    new SessionRequestResultEvent(XmppURI.uri(stanza.getAttribute("to"))));
		}
	    }
	});

    }

    // TODO: deprecate
    public void onSessionCreated(final Listener<XmppURI> listener) {
	SessionRequestResultEvent.bind(connection.getEventBus(), new SessionRequestResultHandler() {
	    @Override
	    public void onSessionRequestResult(final SessionRequestResultEvent event) {
		listener.onEvent(event.getXmppUri());
	    }
	});
    }

    /**
     * Request the session
     * 
     * @param uri
     */
    public void requestSession(final XmppURI uri) {
	final IQ iq = new IQ(IQ.Type.set, uri.getHostURI());
	iq.setFrom(uri);
	iq.setAttribute("id", "im-session-request");
	iq.Includes("session", "urn:ietf:params:xml:ns:xmpp-session");

	connection.send(iq);
    }
}
