package com.calclab.emite.xtesting.handlers;

import com.calclab.emite.xep.disco.client.events.DiscoveryItemsResultEvent;
import com.calclab.emite.xep.disco.client.events.DiscoveryItemsResultHandler;

public class DiscoveryItemsResultTestHandler extends TestHandler<DiscoveryItemsResultEvent> implements
	DiscoveryItemsResultHandler {

    @Override
    public void onDiscoveryItemsResult(DiscoveryItemsResultEvent event) {
	addEvent(event);
    }

}
