package com.calclab.emite.im.client.presence.events;

import com.google.gwt.event.shared.EventHandler;

public interface OwnPresenceChangedHandler extends EventHandler {

    void onOwnPresenceChanged(OwnPresenceChangedEvent event);

}
