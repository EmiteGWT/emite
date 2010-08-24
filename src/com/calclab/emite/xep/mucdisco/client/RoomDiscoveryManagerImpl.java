package com.calclab.emite.xep.mucdisco.client;

import java.util.ArrayList;
import java.util.List;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.xep.disco.client.DiscoveryManager;
import com.calclab.emite.xep.disco.client.FeatureSupportedCallback;
import com.calclab.emite.xep.disco.client.Item;
import com.calclab.emite.xep.disco.client.events.DiscoveryItemsResultEvent;
import com.calclab.emite.xep.disco.client.events.DiscoveryItemsResultHandler;

public class RoomDiscoveryManagerImpl implements RoomDiscoveryManager {

    private final DiscoveryManager discoveryManager;

    public RoomDiscoveryManagerImpl(DiscoveryManager discoveryManager) {
	this.discoveryManager = discoveryManager;
    }

    @Override
    public void discoverRooms(XmppURI targetUri, final ExistingRoomsCallback callback) {
	discoveryManager.sendItemsQuery(targetUri, new DiscoveryItemsResultHandler() {
	    @Override
	    public void onDiscoveryItemsResult(DiscoveryItemsResultEvent event) {
		ArrayList<ExistingRoom> roomItems = new ArrayList<ExistingRoom>();
		if (event.hasResult()) {
		    List<Item> items = event.getResults().getItems();
		    for (Item item : items) {
			roomItems.add(new ExistingRoom(XmppURI.uri(item.jid), item.name));
		    }
		    callback.onExistingRooms(roomItems);
		}
	    }
	});
    }

    @Override
    public void isMucSupported(XmppURI targetUri, FeatureSupportedCallback callback) {
	discoveryManager.areFeaturesSupported(targetUri, callback, "http://jabber.org/protocol/muc");
    }

}
