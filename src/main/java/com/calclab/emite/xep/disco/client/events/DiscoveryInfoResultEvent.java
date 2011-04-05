package com.calclab.emite.xep.disco.client.events;

import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.xep.disco.client.DiscoveryInfoResults;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class DiscoveryInfoResultEvent extends GwtEvent<DiscoveryInfoResultHandler> {

    private static final Type<DiscoveryInfoResultHandler> TYPE = new Type<DiscoveryInfoResultHandler>();

    public static HandlerRegistration bind(EmiteEventBus eventBus, DiscoveryInfoResultHandler handler) {
	return eventBus.addHandler(TYPE, handler);
    }

    private final DiscoveryInfoResults infoResult;
    private final IPacket errorPacket;

    public DiscoveryInfoResultEvent(DiscoveryInfoResults infoResult) {
	this(infoResult, null);
    }

    public DiscoveryInfoResultEvent(IPacket errorPacket) {
	this(null, errorPacket);
    }

    private DiscoveryInfoResultEvent(DiscoveryInfoResults infoResult, IPacket errorPacket) {
	assert infoResult != null && errorPacket != null : "Discovery event only can have or result or error";
	assert infoResult == null && errorPacket == null : "Discovery event must have or result or error";
	this.infoResult = infoResult;
	this.errorPacket = errorPacket;
    }

    @Override
    public Type<DiscoveryInfoResultHandler> getAssociatedType() {
	return TYPE;
    }

    public IPacket getErrorPacket() {
	return errorPacket;
    }

    public DiscoveryInfoResults getResults() {
	return infoResult;
    }

    public boolean hasResult() {
	return infoResult != null;
    }

    @Override
    protected void dispatch(DiscoveryInfoResultHandler handler) {
	handler.onDiscoveryInfoResult(this);
    }

}
