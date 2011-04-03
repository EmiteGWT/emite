package com.calclab.emite.xep.storage.client.events;

import com.google.gwt.event.shared.EventHandler;

public interface PrivateStorageResponseHandler extends EventHandler {

    void onStorageResponse(PrivateStorageResponseEvent event);

}
