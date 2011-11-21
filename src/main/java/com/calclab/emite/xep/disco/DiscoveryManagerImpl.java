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

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;

import com.calclab.emite.base.xml.XMLPacket;
import com.calclab.emite.core.IQCallback;
import com.calclab.emite.core.XmppNamespaces;
import com.calclab.emite.core.XmppURI;
import com.calclab.emite.core.events.IQRequestReceivedEvent;
import com.calclab.emite.core.session.XmppSession;
import com.calclab.emite.core.stanzas.IQ;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.web.bindery.event.shared.EventBus;

@Singleton
public final class DiscoveryManagerImpl implements DiscoveryManager, IQRequestReceivedEvent.Handler {
	
	private final EventBus eventBus;
	private final XmppSession session;

	private final Map<XmppURI, ImmutableSet<Item>> itemCache;
	private final Map<XmppURI, ImmutableSet<Feature>> featureCache;
	private final Map<XmppURI, ImmutableSet<Identity>> identityCache;

	@Inject
	protected DiscoveryManagerImpl(@Named("emite") final EventBus eventBus, final XmppSession session) {
		this.eventBus = checkNotNull(eventBus);
		this.session = checkNotNull(session);

		itemCache = Maps.newHashMap();
		featureCache = Maps.newHashMap();
		identityCache = Maps.newHashMap();
		
		session.addIQRequestReceivedHandler(this);
	}
	
	@Override
	public void onIQRequestReceived(IQRequestReceivedEvent event) {
		final IQ iq = event.getIQ();
		
		if (IQ.Type.get.equals(iq.getType()) && iq.getQuery(XmppNamespaces.DISCO_INFO) != null) {
			// TODO
		} else if (IQ.Type.get.equals(iq.getType()) && iq.getQuery(XmppNamespaces.DISCO_ITEMS) != null) {
			// TODO
		}
	}

	@Override
	public final void sendInfoQuery(final XmppURI targetUri, final DiscoveryInfoCallback handler) {
		sendInfoQuery(targetUri, handler, false);
	}
		
	public final void sendInfoQuery(final XmppURI targetUri, final DiscoveryInfoCallback handler, final boolean useCache) {	
		if (useCache && featureCache.containsKey(targetUri) && identityCache.containsKey(targetUri)) {
			handler.onDiscoveryInfoResult(featureCache.get(targetUri), identityCache.get(targetUri));
			return;
		}
		
		final IQ iq = new IQ(IQ.Type.get);
		iq.setTo(targetUri);
		iq.addQuery(XmppNamespaces.DISCO_INFO);
		
		session.sendIQ("disco", iq, new IQCallback() {
			@Override
			public void onIQSuccess(final IQ iq) {
				final ImmutableSet<Feature> features = parseFeatures(iq.getQuery(XmppNamespaces.DISCO_INFO));
				final ImmutableSet<Identity> identities = parseIdentities(iq.getQuery(XmppNamespaces.DISCO_INFO));
				featureCache.put(targetUri, features);
				identityCache.put(targetUri, identities);
				handler.onDiscoveryInfoResult(features, identities);
			}

			@Override
			public void onIQFailure(final IQ iq) {
				featureCache.remove(targetUri);
				identityCache.remove(targetUri);
				handler.onDiscoveryInfoError(iq.getExtension("error", XmppNamespaces.DISCO_INFO));
			}
		});
	}

	@Override
	public final void sendItemsQuery(final XmppURI targetUri, final DiscoveryItemsCallback handler) {
		sendItemsQuery(targetUri, handler, false);
	}
	
	public final void sendItemsQuery(final XmppURI targetUri, final DiscoveryItemsCallback handler, final boolean useCache) {
		if (useCache && itemCache.containsKey(targetUri)) {
			handler.onDiscoveryItemsResult(itemCache.get(targetUri));
			return;
		}
		
		final IQ iq = new IQ(IQ.Type.get);
		iq.setTo(targetUri);
		iq.addQuery(XmppNamespaces.DISCO_ITEMS);
		
		session.sendIQ("disco", iq, new IQCallback() {
			@Override
			public void onIQSuccess(final IQ iq) {
				final ImmutableSet<Item> items = parseItems(iq.getQuery(XmppNamespaces.DISCO_ITEMS));
				itemCache.put(targetUri, items);
				handler.onDiscoveryItemsResult(items);
			}

			@Override
			public void onIQFailure(final IQ iq) {
				itemCache.remove(targetUri);
				handler.onDiscoveryItemsError(iq.getExtension("error", XmppNamespaces.DISCO_ITEMS));
			}
		});
	}
	
	private static final ImmutableSet<Item> parseItems(final XMLPacket query) {
		final ImmutableSet.Builder<Item> builder = ImmutableSet.builder();
		for (final XMLPacket child : query.getChildren("item")) {
			builder.add(new Item(XmppURI.uri(child.getAttribute("jid")), child.getAttribute("name"), child.getAttribute("node")));
		}
		return builder.build();
	}

	private static final ImmutableSet<Feature> parseFeatures(final XMLPacket query) {
		final ImmutableSet.Builder<Feature> builder = ImmutableSet.builder();
		for (final XMLPacket child : query.getChildren("feature")) {
			builder.add(new Feature(child.getAttribute("var")));
		}
		return builder.build();
	}
	
	private static final ImmutableSet<Identity> parseIdentities(final XMLPacket query) {
		final ImmutableSet.Builder<Identity> builder = ImmutableSet.builder();
		for (final XMLPacket child : query.getChildren("identity")) {
			builder.add(new Identity(child.getAttribute("category"), child.getAttribute("type"), child.getAttribute("name")));
		}
		return builder.build();
	}
}
