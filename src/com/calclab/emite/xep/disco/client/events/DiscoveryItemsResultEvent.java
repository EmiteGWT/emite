package com.calclab.emite.xep.disco.client.events;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.xep.disco.client.DiscoveryItemsResults;
import com.google.gwt.event.shared.GwtEvent;

/**
 * A discovery items result event. Can be a not successful event
 * 
 */
public class DiscoveryItemsResultEvent extends GwtEvent<DiscoveryItemsResultHandler> {

    private static final Type<DiscoveryItemsResultHandler> TYPE = new Type<DiscoveryItemsResultHandler>();
    private final DiscoveryItemsResults itemsResult;
    private final IPacket errorPacket;

    public DiscoveryItemsResultEvent(DiscoveryItemsResults infoResult) {
	this(infoResult, null);
    }

    public DiscoveryItemsResultEvent(IPacket errorPacket) {
	this(null, errorPacket);
    }

    private DiscoveryItemsResultEvent(DiscoveryItemsResults itemsResult, IPacket errorPacket) {
	assert itemsResult != null && errorPacket != null : "Discovery event only can have or result or error";
	assert itemsResult == null && errorPacket == null : "Discovery event must have or result or error";
	this.itemsResult = itemsResult;
	this.errorPacket = errorPacket;
    }

    @Override
    public GwtEvent.Type<DiscoveryItemsResultHandler> getAssociatedType() {
	return TYPE;
    }

    public IPacket getErrorPacket() {
	return errorPacket;
    }

    public DiscoveryItemsResults getResults() {
	return itemsResult;
    }

    public boolean hasResult() {
	return itemsResult != null;
    }

    @Override
    protected void dispatch(DiscoveryItemsResultHandler handler) {
	handler.onDiscoveryItemsResult(this);
    }

}
