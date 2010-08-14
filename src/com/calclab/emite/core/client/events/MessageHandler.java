package com.calclab.emite.core.client.events;

import com.google.gwt.event.shared.EventHandler;

public interface MessageHandler extends EventHandler {
    void onPacketEvent(MessageEvent event);
}
