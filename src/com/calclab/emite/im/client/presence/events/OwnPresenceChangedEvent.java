package com.calclab.emite.im.client.presence.events;

import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.core.client.xmpp.stanzas.Presence;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * The current user presence has changed.
 * 
 */
public class OwnPresenceChangedEvent extends GwtEvent<OwnPresenceChangedHandler> {

    private static final Type<OwnPresenceChangedHandler> TYPE = new Type<OwnPresenceChangedHandler>();

    public static HandlerRegistration bind(EmiteEventBus eventBus, OwnPresenceChangedHandler handler) {
	return eventBus.addHandler(TYPE, handler);
    }
    private final Presence currentPresence;

    private final Presence oldPresence;

    public OwnPresenceChangedEvent(Presence oldPresence, Presence currentPresence) {
	this.oldPresence = oldPresence;
	this.currentPresence = currentPresence;
    }

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<OwnPresenceChangedHandler> getAssociatedType() {
	return TYPE;
    }

    public Presence getCurrentPresence() {
	return currentPresence;
    }

    public Presence getOldPresence() {
	return oldPresence;
    }

    @Override
    public String toDebugString() {
	return super.toDebugString() + currentPresence.toString();
    }

    @Override
    protected void dispatch(OwnPresenceChangedHandler handler) {
	handler.onOwnPresenceChanged(this);
    }

}
