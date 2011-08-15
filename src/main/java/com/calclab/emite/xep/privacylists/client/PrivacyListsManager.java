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

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.xmpp.session.IQResponseHandler;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.calclab.emite.core.client.xmpp.stanzas.IQ;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.google.inject.Inject;

/**
 * Will (i hope!) implement http://www.xmpp.org/extensions/xep-0016.html
 */
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
	public void blockUserBasedOnJID(final String listName, final XmppURI uri, final Integer order) {
		final IQ iq = new IQ(IQ.Type.set);
		final IPacket list = iq.addQuery("jabber:iq:privacy").addChild("list", null);
		list.With("name", listName);
		list.addChild("item", null).With("type", "jid").With("value", uri.getJID().toString()).With("action", "deny").With("order", order.toString());

		session.sendIQ("privacyLists", iq, new IQResponseHandler() {
			@Override
			public void onIQ(final IQ iq) {
				// here you can handle the response... i think in this case is
				// not needed
			}
		});

	}
}
