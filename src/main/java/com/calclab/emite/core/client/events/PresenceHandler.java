package com.calclab.emite.core.client.events;

import com.google.gwt.event.shared.EventHandler;

public interface PresenceHandler extends EventHandler {
    void onPresence(PresenceEvent event);
}
