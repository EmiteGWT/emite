package com.calclab.emite.xep.muc.client.events;

import com.calclab.emite.core.client.events.ChangedEvent;
import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.xep.muc.client.Occupant;
import com.google.gwt.event.shared.HandlerRegistration;

public class OccupantChangedEvent extends ChangedEvent<OccupantChangedHandler> {

    private static final Type<OccupantChangedHandler> TYPE = new Type<OccupantChangedHandler>();

    public static HandlerRegistration bind(EmiteEventBus eventBus, OccupantChangedHandler handler) {
	return eventBus.addHandler(TYPE, handler);
    }

    private final Occupant occupant;

    public OccupantChangedEvent(String changeType, Occupant occupant) {
	super(TYPE, changeType);
	this.occupant = occupant;
    }

    public Occupant getOccupant() {
	return occupant;
    }

    @Override
    public String toDebugString() {
	return super.toDebugString() + occupant;
    }

    @Override
    protected void dispatch(OccupantChangedHandler handler) {
	handler.onOccupantChanged(this);
    }

}
