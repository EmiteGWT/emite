package com.calclab.emite.core.client.events;

import com.google.gwt.event.shared.EventHandler;

public interface PacketHandler extends EventHandler {

    void onPacket(PacketEvent event);

}
