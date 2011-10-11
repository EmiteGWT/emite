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

package com.calclab.emite.xep.privacylists.client;

import com.calclab.emite.core.client.session.XmppSession;
import com.calclab.emite.core.client.stanzas.IQ;
import com.calclab.emite.core.client.stanzas.XmppURI;
import com.calclab.emite.core.client.xml.XMLPacket;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Will (i hope!) implement http://www.xmpp.org/extensions/xep-0016.html
 */
@Singleton
public class PrivacyListsManager {
	private final XmppSession session;

	@Inject
	public PrivacyListsManager(final XmppSession session) {
		this.session = session;
	}

	/**
	 * Block incoming messages from other entity based on the entity's JID.
	 * 
	 * @see http://www.xmpp.org/extensions/xep-0016.html#protocol-message
	 * 
	 * @param listName
	 *            is that necessary?
	 * @param uri
	 *            the other entity jid
	 * @param order
	 *            i din't read the spec... is that necessary?
	 */
	public void blockUserBasedOnJID(final String listName, final XmppURI uri, final int order) {
		final IQ iq = new IQ(IQ.Type.set);
		final XMLPacket list = iq.addChild("query", "jabber:iq:privacy").addChild("list", null);
		list.setAttribute("name", listName);
		final XMLPacket item = list.addChild("item", null);
		item.setAttribute("type", "jid");
		item.setAttribute("value", uri.getJID().toString());
		item.setAttribute("action", "deny");
		item.setAttribute("order", String.valueOf(order));

		session.sendIQ("privacyLists", iq, null);
		// here you can handle the response... i think in this case is not needed
	}
}
