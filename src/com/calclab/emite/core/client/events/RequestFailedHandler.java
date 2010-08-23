package com.calclab.emite.core.client.events;

import com.google.gwt.event.shared.EventHandler;

public interface RequestFailedHandler extends EventHandler {

    void onRequestFailed(RequestFailedEvent event);

}
