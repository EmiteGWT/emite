package com.calclab.emite.im.client.roster.events;

import com.calclab.emite.core.client.events.ChangedEvent;
import com.calclab.emite.im.client.roster.RosterGroup;

public class RosterGroupChangedEvent extends ChangedEvent<RosterGroupChangedHandler> {

    private static final Type<RosterGroupChangedHandler> TYPE = new Type<RosterGroupChangedHandler>();
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
