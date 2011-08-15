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

package com.calclab.emite.xep.vcard.client;

import com.calclab.emite.core.client.xmpp.session.IQResponseHandler;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.calclab.emite.core.client.xmpp.stanzas.IQ;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.xep.vcard.client.events.VCardResponseEvent;
import com.calclab.emite.xep.vcard.client.events.VCardResponseHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Implements VCardManager
 * 
 * @see VCardManager
 */
@Singleton
public class VCardManagerImpl implements VCardManager {
	private static final String ID_PREFIX = "vcard";
	private final XmppSession session;

	@Inject
	public VCardManagerImpl(final XmppSession session) {
		this.session = session;
	}

	@Override
	public HandlerRegistration addVCardResponseHandler(final VCardResponseHandler handler) {
		return VCardResponseEvent.bind(session.getEventBus(), handler);
	}

	@Override
	public void getUserVCard(final XmppURI userJid, final VCardResponseHandler handler) {
		final IQ iq = new IQ(IQ.Type.get);
		iq.addChild(VCard.VCARD, VCard.DATA_XMLS);
		iq.setFrom(session.getCurrentUserURI());
		iq.setTo(userJid);

		session.sendIQ(ID_PREFIX, iq, new IQResponseHandler() {
			@Override
			public void onIQ(final IQ iq) {
				handleVCard(iq, handler);
			}
		});

	}

	@Override
	public void requestOwnVCard(final VCardResponseHandler handler) {
		final IQ iq = new IQ(IQ.Type.get);
		iq.addChild(VCard.VCARD, VCard.DATA_XMLS);
		iq.setFrom(session.getCurrentUserURI());
		session.sendIQ(ID_PREFIX, iq, new IQResponseHandler() {
			@Override
			public void onIQ(final IQ iq) {
				handleVCard(iq, handler);
			}
		});

	}

	@Override
	public void updateOwnVCard(final VCard vcard, final VCardResponseHandler handler) {
		final IQ iq = new IQ(IQ.Type.set);
		iq.addChild(vcard);
		session.sendIQ(ID_PREFIX, iq, new IQResponseHandler() {
			@Override
			public void onIQ(final IQ iq) {
				handleVCard(iq, handler);
			}
		});

	}

	protected void handleVCard(final IQ result, final VCardResponseHandler handler) {
		final VCardResponse response = new VCardResponse(result);
		final VCardResponseEvent event = new VCardResponseEvent(response);
		if (handler != null) {
			handler.onVCardResponse(event);
		}
		session.getEventBus().fireEvent(event);
	}
}
