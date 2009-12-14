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
package com.calclab.emite.core.client.xmpp.resource;

import com.calclab.emite.core.client.bosh.Connection;
import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.xmpp.stanzas.IQ;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.suco.client.events.Event;
import com.calclab.suco.client.events.Listener;

public class ResourceBindingManager {
    private final Event<XmppURI> onBinded;
    private final Connection connection;

    public ResourceBindingManager(final Connection connection) {
	this.connection = connection;
	this.onBinded = new Event<XmppURI>("resourceBindingManager:onBinded");

	connection.onStanzaReceived(new Listener<IPacket>() {
	    public void onEvent(final IPacket received) {
		if ("bind-resource".equals(received.getAttribute("id"))) {
		    final String jid = received.getFirstChild("bind").getFirstChild("jid").getText();
		    onBinded.fire(XmppURI.uri(jid));
		}
	    }
	});
    }

    public void bindResource(final String resource) {
	final IQ iq = new IQ(IQ.Type.set);
	iq.setId("bind-resource");
	iq.addChild("bind", "urn:ietf:params:xml:ns:xmpp-bind").addChild("resource", null).setText(resource);

	connection.send(iq);
    }

    public void onBinded(final Listener<XmppURI> listener) {
	onBinded.add(listener);
    }

}
