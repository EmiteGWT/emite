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

package com.calclab.emite.xep.vcard;

import com.calclab.emite.core.IQCallback;
import com.calclab.emite.core.XmppURI;
import com.calclab.emite.core.session.XmppSession;
import com.calclab.emite.core.stanzas.IQ;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * Implements VCardManager
 * 
 * @see VCardManager
 */
@Singleton
public class VCardManagerImpl implements VCardManager {
	private static final String ID_PREFIX = "vcard";

	private final EventBus eventBus;
	private final XmppSession session;

	@Inject
	public VCardManagerImpl(@Named("emite") final EventBus eventBus, final XmppSession session) {
		this.eventBus = eventBus;
		this.session = session;
	}

	@Override
	public HandlerRegistration addVCardResponseHandler(final VCardResponseEvent.Handler handler) {
		return eventBus.addHandlerToSource(VCardResponseEvent.TYPE, this, handler);
	}

	@Override
	public void getUserVCard(final XmppURI userJid, final VCardResponseEvent.Handler handler) {
		final IQ iq = new IQ(IQ.Type.get);
		iq.addChild(VCard.VCARD, VCard.DATA_XMLS);
		iq.setFrom(session.getCurrentUserURI());
		iq.setTo(userJid);

		session.sendIQ(ID_PREFIX, iq, new IQCallback() {
			@Override
			public void onIQ(final IQ iq) {
				handleVCard(iq, handler);
			}
		});

	}

	@Override
	public void requestOwnVCard(final VCardResponseEvent.Handler handler) {
		final IQ iq = new IQ(IQ.Type.get);
		iq.addChild(VCard.VCARD, VCard.DATA_XMLS);
		iq.setFrom(session.getCurrentUserURI());
		session.sendIQ(ID_PREFIX, iq, new IQCallback() {
			@Override
			public void onIQ(final IQ iq) {
				handleVCard(iq, handler);
			}
		});

	}

	@Override
	public void updateOwnVCard(final VCard vcard, final VCardResponseEvent.Handler handler) {
		final IQ iq = new IQ(IQ.Type.set);
		iq.addChild(vcard);
		session.sendIQ(ID_PREFIX, iq, new IQCallback() {
			@Override
			public void onIQ(final IQ iq) {
				handleVCard(iq, handler);
			}
		});

	}

	protected void handleVCard(final IQ result, final VCardResponseEvent.Handler handler) {
		final VCardResponse response = new VCardResponse(result);
		final VCardResponseEvent event = new VCardResponseEvent(response);
		if (handler != null) {
			handler.onVCardResponse(event);
		}
		eventBus.fireEventFromSource(event, this);
	}
}
