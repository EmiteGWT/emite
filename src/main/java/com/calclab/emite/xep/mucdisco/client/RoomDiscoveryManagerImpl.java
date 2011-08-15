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

package com.calclab.emite.xep.mucdisco.client;

import java.util.ArrayList;
import java.util.List;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.xep.disco.client.DiscoveryManager;
import com.calclab.emite.xep.disco.client.FeatureSupportedCallback;
import com.calclab.emite.xep.disco.client.Item;
import com.calclab.emite.xep.disco.client.events.DiscoveryItemsResultEvent;
import com.calclab.emite.xep.disco.client.events.DiscoveryItemsResultHandler;
import com.google.inject.Inject;

public class RoomDiscoveryManagerImpl implements RoomDiscoveryManager {

	private final DiscoveryManager discoveryManager;

	@Inject
	public RoomDiscoveryManagerImpl(final DiscoveryManager discoveryManager) {
		this.discoveryManager = discoveryManager;
	}

	@Override
	public void discoverRooms(final XmppURI targetUri, final ExistingRoomsCallback callback) {
		discoveryManager.sendItemsQuery(targetUri, new DiscoveryItemsResultHandler() {
			@Override
			public void onDiscoveryItemsResult(final DiscoveryItemsResultEvent event) {
				final ArrayList<ExistingRoom> roomItems = new ArrayList<ExistingRoom>();
				if (event.hasResult()) {
					final List<Item> items = event.getResults().getItems();
					for (final Item item : items) {
						roomItems.add(new ExistingRoom(XmppURI.uri(item.jid), item.name));
					}
					callback.onExistingRooms(roomItems);
				}
			}
		});
	}

	@Override
	public void isMucSupported(final XmppURI targetUri, final FeatureSupportedCallback callback) {
		discoveryManager.areFeaturesSupported(targetUri, callback, "http://jabber.org/protocol/muc");
	}

}
