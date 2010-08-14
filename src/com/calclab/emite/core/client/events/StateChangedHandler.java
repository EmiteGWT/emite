package com.calclab.emite.core.client.events;

import com.google.gwt.event.shared.EventHandler;

public interface StateChangedHandler extends EventHandler {
    void onStateChanged(StateChangedEvent event);
}
