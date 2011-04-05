package com.calclab.emite.core.client.events;

import com.google.gwt.event.shared.EventHandler;

public interface IQHandler extends EventHandler {
    void onPacket(IQEvent event);
}
