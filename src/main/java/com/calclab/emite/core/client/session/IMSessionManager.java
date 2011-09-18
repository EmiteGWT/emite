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

package com.calclab.emite.core.client.session;

import com.calclab.emite.core.client.conn.XmppConnection;
import com.calclab.emite.core.client.events.SessionRequestResultEvent;
import com.calclab.emite.core.client.events.StanzaReceivedEvent;
import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.stanzas.IQ;
import com.calclab.emite.core.client.stanzas.XmppURI;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * Handle the IM session request. Used by XmppSession (not need to be used by
 * clients)
 * 
 * @see http://www.xmpp.org/extensions/xep-0206.html#preconditions-sasl
 */
@Singleton
public class IMSessionManager implements StanzaReceivedEvent.Handler {
	
	private final EventBus eventBus;
	private final XmppConnection connection;

	@Inject
	public IMSessionManager(@Named("emite") final EventBus eventBus, final XmppConnection connection) {
		this.eventBus = eventBus;
		this.connection = connection;

		connection.addStanzaReceivedHandler(this);
	}
	
	public HandlerRegistration addSessionRequestResultHandler(final SessionRequestResultEvent.Handler handler) {
		return eventBus.addHandlerToSource(SessionRequestResultEvent.TYPE, this, handler);
	}
	
	@Override
	public void onStanzaReceived(final StanzaReceivedEvent event) {
		final IPacket stanza = event.getStanza();
		if ("im-session-request".equals(stanza.getAttribute("id"))) {
			eventBus.fireEventFromSource(new SessionRequestResultEvent(XmppURI.uri(stanza.getAttribute("to"))), this);
		}
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
