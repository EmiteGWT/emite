package com.calclab.emite.core.client.events;

import com.calclab.emite.core.client.xmpp.stanzas.Presence;
import com.google.gwt.event.shared.HandlerRegistration;

public class PresenceReceivedEvent extends PresenceEvent {

    private static final Type<PresenceHandler> TYPE = new Type<PresenceHandler>();

    public static HandlerRegistration bind(EmiteEventBus eventBus, PresenceHandler handler) {
	return eventBus.addHandler(TYPE, handler);
    }

    public PresenceReceivedEvent(final Presence presence) {
	super(TYPE, presence);
    }

}
