package com.calclab.emite.im.client.roster.events;

import com.calclab.emite.core.client.events.ChangedEvent;
import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.im.client.roster.RosterGroup;
import com.google.gwt.event.shared.HandlerRegistration;

public class RosterGroupChangedEvent extends ChangedEvent<RosterGroupChangedHandler> {

    private static final Type<RosterGroupChangedHandler> TYPE = new Type<RosterGroupChangedHandler>();

    public static HandlerRegistration bind(EmiteEventBus eventBus, RosterGroupChangedHandler handler) {
	return eventBus.addHandler(TYPE, handler);
    }

    private final RosterGroup rosterGroup;

    public RosterGroupChangedEvent(String changeType, RosterGroup rosterGroup) {
	super(TYPE, changeType);
	this.rosterGroup = rosterGroup;
    }

    public RosterGroup getRosterGroup() {
	return rosterGroup;
    }

    @Override
    protected void dispatch(RosterGroupChangedHandler handler) {
	handler.onGroupChanged(this);
    }

}
