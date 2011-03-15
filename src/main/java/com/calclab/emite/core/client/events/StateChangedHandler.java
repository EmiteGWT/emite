package com.calclab.emite.core.client.events;

import com.google.gwt.event.shared.EventHandler;

/**
 * Handle a StateChangedEvent
 * 
 * @see StateChangedEvent
 * 
 */
public interface StateChangedHandler extends EventHandler {
    /**
     * Called when the state has changed
     * 
     * @param event
     */
    void onStateChanged(StateChangedEvent event);
}
