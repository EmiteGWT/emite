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

package com.calclab.emite.xep.disco;

import java.util.Map;

import com.calclab.emite.core.IQCallback;
import com.calclab.emite.core.XmppURI;
import com.calclab.emite.core.session.XmppSession;
import com.calclab.emite.core.stanzas.IQ;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;

@Singleton
public class DiscoveryManagerImpl implements DiscoveryManager {

	private final EventBus eventBus;
	private final XmppSession session;

	private final Map<XmppURI, DiscoveryInfoResults> infoResults;
	private final Map<XmppURI, DiscoveryItemsResults> itemsResults;

	@Inject
	protected DiscoveryManagerImpl(@Named("emite") final EventBus eventBus, final XmppSession session) {
		this.eventBus = eventBus;
		this.session = session;

		infoResults = Maps.newHashMap();
		itemsResults = Maps.newHashMap();
	}

	@Override
	public HandlerRegistration addDiscoveryInfoResultHandler(final DiscoveryInfoResultEvent.Handler handler) {
		return eventBus.addHandlerToSource(DiscoveryInfoResultEvent.TYPE, this, handler);
	}

	@Override
	public void areFeaturesSupported(final XmppURI targetUri, final FeatureSupportedHandler callback, final String... featuresName) {
		sendInfoQuery(targetUri, new DiscoveryInfoResultEvent.Handler() {
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
	public void sendInfoQuery(final XmppURI targetUri, final DiscoveryInfoResultEvent.Handler handler) {
		final DiscoveryInfoResults cached = infoResults.get(targetUri);
		if (cached != null) {
			if (handler != null) {
				handler.onDiscoveryInfoResult(new DiscoveryInfoResultEvent(cached));
			}
		} else {
			final IQ iq = new IQ(IQ.Type.get);
			iq.setTo(targetUri);
			iq.addChild("query", "http://jabber.org/protocol/disco#info");
			session.sendIQ("disco", iq, new IQCallback() {
				@Override
				public void onIQSuccess(final IQ iq) {
					final DiscoveryInfoResults infoResult = new DiscoveryInfoResults(iq);
					infoResults.put(targetUri, infoResult);

					final DiscoveryInfoResultEvent event = new DiscoveryInfoResultEvent(infoResult);
					if (handler != null) {
						handler.onDiscoveryInfoResult(event);
					}
					eventBus.fireEventFromSource(event, this);
				}

				@Override
				public void onIQFailure(final IQ iq) {
					final DiscoveryInfoResultEvent event = new DiscoveryInfoResultEvent(iq.getChild("error", "http://jabber.org/protocol/disco#info"));
					if (handler != null) {
						handler.onDiscoveryInfoResult(event);
					}
					eventBus.fireEventFromSource(event, this);
				}
			});
		}
	}

	@Override
	public void sendItemsQuery(final XmppURI targetUri, final DiscoveryItemsResultEvent.Handler handler) {
		final DiscoveryItemsResults cached = itemsResults.get(targetUri);
		if (cached != null) {
			if (handler != null) {
				handler.onDiscoveryItemsResult(new DiscoveryItemsResultEvent(cached));
			}
		} else {
			final IQ iq = new IQ(IQ.Type.get);
			iq.setTo(targetUri);
			iq.addChild("query", "http://jabber.org/protocol/disco#items");
			session.sendIQ("disco", iq, new IQCallback() {
				@Override
				public void onIQSuccess(final IQ iq) {
					final DiscoveryItemsResults itemsResult = new DiscoveryItemsResults(iq);
					itemsResults.put(targetUri, itemsResult);

					final DiscoveryItemsResultEvent event = new DiscoveryItemsResultEvent(itemsResult);
					if (handler != null) {
						handler.onDiscoveryItemsResult(event);
					}
					eventBus.fireEventFromSource(event, this);
				}

				@Override
				public void onIQFailure(final IQ iq) {
					final DiscoveryItemsResultEvent event = new DiscoveryItemsResultEvent(iq.getChild("error", "http://jabber.org/protocol/disco#items"));
					if (handler != null) {
						handler.onDiscoveryItemsResult(event);
					}
					eventBus.fireEventFromSource(event, this);
				}
			});
		}
	}

}
