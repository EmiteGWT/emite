package com.calclab.emite.core.client.events;

import com.calclab.emite.core.client.xmpp.stanzas.Presence;
import com.google.gwt.event.shared.GwtEvent;

public abstract class PresenceEvent extends GwtEvent<PresenceHandler> {

    private final Presence presence;
    private final Type<PresenceHandler> associatedType;

    public PresenceEvent(final Type<PresenceHandler> associatedType, final Presence presence) {
	assert associatedType != null : "Associated type can't be null in PresenceEvent";
	assert presence != null : "Presence can't be null in PresenceEvent";
	this.associatedType = associatedType;
	this.presence = presence;
    }

    @Override
    public Type<PresenceHandler> getAssociatedType() {
	return associatedType;
    }

    public Presence getPresence() {
	return presence;
    }

    @Override
    public String toDebugString() {
	return super.toDebugString() + presence;
    }

    @Override
    protected void dispatch(final PresenceHandler handler) {
	handler.onPresence(this);
    }
}
