package com.calclab.emite.core.client.events;

import com.calclab.emite.core.client.packet.IPacket;
import com.google.gwt.event.shared.HandlerRegistration;

public class BeforeStanzaSendEvent extends PacketEvent {
    private static final Type<PacketHandler> TYPE = new Type<PacketHandler>();

    public static HandlerRegistration bind(EmiteEventBus eventBus, PacketHandler handler) {
	return eventBus.addHandler(TYPE, handler);
    }

    public BeforeStanzaSendEvent(IPacket packet) {
	super(TYPE, packet);
    }

}
