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

package com.calclab.emite.xep.disco.client;

import java.util.HashMap;

import com.calclab.emite.core.client.packet.MatcherFactory;
import com.calclab.emite.core.client.packet.PacketMatcher;
import com.calclab.emite.core.client.xmpp.session.IQResponseHandler;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.calclab.emite.core.client.xmpp.stanzas.IQ;
import com.calclab.emite.core.client.xmpp.stanzas.IQ.Type;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.xep.disco.client.events.DiscoveryInfoResultEvent;
import com.calclab.emite.xep.disco.client.events.DiscoveryInfoResultHandler;
import com.calclab.emite.xep.disco.client.events.DiscoveryItemsResultEvent;
import com.calclab.emite.xep.disco.client.events.DiscoveryItemsResultHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.inject.Inject;

public class DiscoveryManagerImpl implements DiscoveryManager {
	private final XmppSession session;
	private final HashMap<XmppURI, DiscoveryInfoResults> infoResults;
	private final HashMap<XmppURI, DiscoveryItemsResults> itemsResults;
	public static final PacketMatcher ERROR_MATCHER = MatcherFactory.byName("error");

	@Inject
	public DiscoveryManagerImpl(final XmppSession xmppSession) {
		session = xmppSession;
		infoResults = new HashMap<XmppURI, DiscoveryInfoResults>();
		itemsResults = new HashMap<XmppURI, DiscoveryItemsResults>();
	}

	@Override
	public HandlerRegistration addDiscoveryInfoResultHandler(final DiscoveryInfoResultHandler handler) {
		return DiscoveryInfoResultEvent.bind(session.getEventBus(), handler);
	}

	@Override
	public void areFeaturesSupported(final XmppURI targetUri, final FeatureSupportedCallback callback, final String... featuresName) {
		sendInfoQuery(targetUri, new DiscoveryInfoResultHandler() {
			@Override
			public void onDiscoveryInfoResult(final DiscoveryInfoResultEvent event) {
				if (event.hasResult()) {
					callback.onFeaturesSupported(event.getResults().areFeaturedSupported(featuresName));
				} else {
					callback.onFeaturesSupported(false);
				}
			}
		});
	}

	@Override
	public void sendInfoQuery(final XmppURI targetUri, final DiscoveryInfoResultHandler handler) {
		final DiscoveryInfoResults cached = infoResults.get(targetUri);
		if (cached != null) {
			if (handler != null) {
				handler.onDiscoveryInfoResult(new DiscoveryInfoResultEvent(cached));
			}
		} else {
			final IQ iq = new IQ(Type.get, targetUri);
			iq.addQuery("http://jabber.org/protocol/disco#info");
			session.sendIQ("disco", iq, new IQResponseHandler() {
				@Override
				public void onIQ(final IQ iq) {
					DiscoveryInfoResultEvent event;
					if (IQ.isSuccess(iq)) {
						final DiscoveryInfoResults infoResult = new DiscoveryInfoResults(iq);
						infoResults.put(targetUri, infoResult);
						event = new DiscoveryInfoResultEvent(infoResult);
					} else {
						event = new DiscoveryInfoResultEvent(iq.getFirstChild(ERROR_MATCHER));
					}
					if (handler != null) {
						handler.onDiscoveryInfoResult(event);
					}
					session.getEventBus().fireEvent(event);
				}
			});
		}
	}

	@Override
	public void sendItemsQuery(final XmppURI targetUri, final DiscoveryItemsResultHandler handler) {
		final DiscoveryItemsResults cached = itemsResults.get(targetUri);
		if (cached != null) {
			if (handler != null) {
				handler.onDiscoveryItemsResult(new DiscoveryItemsResultEvent(cached));
			}
		} else {
			final IQ iq = new IQ(Type.get, targetUri);
			iq.addQuery("http://jabber.org/protocol/disco#items");
			session.sendIQ("disco", iq, new IQResponseHandler() {
				@Override
				public void onIQ(final IQ iq) {
					DiscoveryItemsResultEvent event;
					if (IQ.isSuccess(iq)) {
						final DiscoveryItemsResults itemsResult = new DiscoveryItemsResults(iq);
						itemsResults.put(targetUri, itemsResult);
						event = new DiscoveryItemsResultEvent(itemsResult);
					} else {
						event = new DiscoveryItemsResultEvent(iq.getFirstChild(ERROR_MATCHER));
					}
					if (handler != null) {
						handler.onDiscoveryItemsResult(event);
					}
					session.getEventBus().fireEvent(event);
				}
			});
		}
	}

}
