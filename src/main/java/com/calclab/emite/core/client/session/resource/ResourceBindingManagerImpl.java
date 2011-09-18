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

package com.calclab.emite.core.client.session.resource;

import com.calclab.emite.core.client.conn.XmppConnection;
import com.calclab.emite.core.client.events.StanzaReceivedEvent;
import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.stanzas.IQ;
import com.calclab.emite.core.client.stanzas.XmppURI;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;

@Singleton
public class ResourceBindingManagerImpl implements ResourceBindingManager, StanzaReceivedEvent.Handler {

	private final EventBus eventBus;
	private final XmppConnection connection;

	@Inject
	public ResourceBindingManagerImpl(@Named("emite") EventBus eventBus, final XmppConnection connection) {
		this.eventBus = eventBus;
		this.connection = connection;

		connection.addStanzaReceivedHandler(this);
	}
	
	public HandlerRegistration addResourceBindResultHandler(final ResourceBindResultEvent.Handler handler) {
		return eventBus.addHandlerToSource(ResourceBindResultEvent.TYPE, this, handler);
	}
	
	@Override
	public void onStanzaReceived(final StanzaReceivedEvent event) {
		final IPacket received = event.getStanza();
		if ("bind-resource".equals(received.getAttribute("id"))) {
			final String jid = received.getFirstChild("bind").getFirstChild("jid").getText();
			eventBus.fireEventFromSource(new ResourceBindResultEvent(XmppURI.uri(jid)), this);
		}
	}

	public void bindResource(final String resource) {
		final IQ iq = new IQ(IQ.Type.set);
		iq.setId("bind-resource");
		iq.addChild("bind", "urn:ietf:params:xml:ns:xmpp-bind").addChild("resource", null).setText(resource);

		connection.send(iq);
	}

}
