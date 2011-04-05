package com.calclab.emite.core.client.events;

import com.calclab.emite.core.client.packet.IPacket;
import com.google.gwt.event.shared.GwtEvent;

public abstract class PacketEvent extends GwtEvent<PacketHandler> {
    private final Type<PacketHandler> type;
    private final IPacket packet;

    public PacketEvent(Type<PacketHandler> type, IPacket packet) {
	this.type = type;
	this.packet = packet;
    }

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<PacketHandler> getAssociatedType() {
	return type;
    }

    public IPacket getPacket() {
	return packet;
    }

    @Override
    protected void dispatch(PacketHandler handler) {
	handler.onPacket(this);
    }

}
