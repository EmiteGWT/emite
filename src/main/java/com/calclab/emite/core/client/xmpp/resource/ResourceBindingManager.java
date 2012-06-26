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

package com.calclab.emite.core.client.xmpp.resource;

import java.util.HashMap;
import com.calclab.emite.core.client.LoginXmppMap;
import com.calclab.emite.core.client.MultiInstance;
import com.calclab.emite.core.client.conn.StanzaEvent;
import com.calclab.emite.core.client.conn.StanzaHandler;
import com.calclab.emite.core.client.conn.XmppConnection;
import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.xmpp.stanzas.IQ;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.calclab.emite.core.client.LoginXmpp;

//@Singleton
public class ResourceBindingManager  implements MultiInstance {
    private XmppConnection connection;
	private HashMap<String, LoginXmpp> loginXmppMap;

	@Inject
    public ResourceBindingManager( final @LoginXmppMap  HashMap <String, LoginXmpp> loginXmppMap) {
    	this.loginXmppMap = loginXmppMap;
	}

	public void bindResource(final String resource) {
		final IQ iq = new IQ(IQ.Type.set);
		iq.setId("bind-resource");
		iq.addChild("bind", "urn:ietf:params:xml:ns:xmpp-bind").addChild("resource", null).setText(resource);

		connection.send(iq);
	}

	@Override
	public void setInstanceId(String instanceId) {		
		LoginXmpp loginXmpp = loginXmppMap.get(instanceId);
		this.connection = loginXmpp.xmppConnection;    	

		connection.addStanzaReceivedHandler(new StanzaHandler() {
			@Override
			public void onStanza(final StanzaEvent event) {
				final IPacket received = event.getStanza();
				if ("bind-resource".equals(received.getAttribute("id"))) {
					final String jid = received.getFirstChild("bind").getFirstChild("jid").getText();
					connection.getEventBus().fireEvent(new ResourceBindResultEvent(XmppURI.uri(jid)));
				}
			}
		});
	}

}
