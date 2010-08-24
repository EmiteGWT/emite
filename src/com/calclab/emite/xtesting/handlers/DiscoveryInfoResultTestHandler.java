package com.calclab.emite.xtesting.handlers;

import com.calclab.emite.xep.disco.client.events.DiscoveryInfoResultEvent;
import com.calclab.emite.xep.disco.client.events.DiscoveryInfoResultHandler;

public class DiscoveryInfoResultTestHandler extends TestHandler<DiscoveryInfoResultEvent> implements
	DiscoveryInfoResultHandler {

    @Override
    public void onDiscoveryInfoResult(DiscoveryInfoResultEvent event) {
	addEvent(event);
    }

}
