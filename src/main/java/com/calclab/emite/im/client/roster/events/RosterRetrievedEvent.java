package com.calclab.emite.im.client.roster.events;

import java.util.Collection;

import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.im.client.roster.RosterItem;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class RosterRetrievedEvent extends GwtEvent<RosterRetrievedHandler> {

    private static final Type<RosterRetrievedHandler> TYPE = new Type<RosterRetrievedHandler>();

    public static HandlerRegistration bind(EmiteEventBus eventBus, RosterRetrievedHandler handler) {
	return eventBus.addHandler(TYPE, handler);
    }

    private final Collection<RosterItem> rosterItems;

    public RosterRetrievedEvent(Collection<RosterItem> rosterItems) {
	this.rosterItems = rosterItems;
    }

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<RosterRetrievedHandler> getAssociatedType() {
	return TYPE;
    }

    public Collection<RosterItem> getRosterItems() {
	return rosterItems;
    }

    @Override
    protected void dispatch(RosterRetrievedHandler handler) {
	handler.onRosterRetrieved(this);
    }

}
