package com.calclab.emite.xtesting.handlers;

import com.calclab.emite.xep.storage.client.events.PrivateStorageResponseEvent;
import com.calclab.emite.xep.storage.client.events.PrivateStorageResponseHandler;

public class PrivateStorageResponseTestHandler extends TestHandler<PrivateStorageResponseEvent> implements
	PrivateStorageResponseHandler {

    @Override
    public void onStorageResponse(PrivateStorageResponseEvent event) {
	addEvent(event);
    }

}
