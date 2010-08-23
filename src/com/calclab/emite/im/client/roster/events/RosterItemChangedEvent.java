package com.calclab.emite.im.client.roster.events;

import com.calclab.emite.core.client.events.ChangedEvent;
import com.calclab.emite.core.client.events.ChangedEvent.ChangeTypes;
import com.calclab.emite.im.client.roster.RosterItem;

/**
 * A roster item has changed
 * 
 * @see ChangeTypes
 * @see ChangedEvent
 */
public class RosterItemChangedEvent extends ChangedEvent<RosterItemChangedHandler> {

    private static final Type<RosterItemChangedHandler> TYPE = new Type<RosterItemChangedHandler>();
    private final RosterItem rosterItem;

    public RosterItemChangedEvent(String changeType, RosterItem rosterItem) {
	super(TYPE, changeType);
	this.rosterItem = rosterItem;
    }

    public RosterItem getRosterItem() {
	return rosterItem;
    }

    @Override
    protected void dispatch(RosterItemChangedHandler handler) {
	handler.onRosterItemChanged(this);
    }

}
