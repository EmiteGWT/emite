package com.calclab.emite.xep.avatar.client.events;

import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.core.client.events.PresenceEvent;
import com.calclab.emite.core.client.events.PresenceHandler;
import com.calclab.emite.core.client.xmpp.stanzas.Presence;
import com.google.gwt.event.shared.HandlerRegistration;

public class HashPresenceReceivedEvent extends PresenceEvent {

    private static final Type<PresenceHandler> TYPE = new Type<PresenceHandler>();

    public static HandlerRegistration bind(EmiteEventBus eventBus, PresenceHandler handler) {
	return eventBus.addHandler(TYPE, handler);
    }

    public HashPresenceReceivedEvent(Presence presence) {
	super(TYPE, presence);
    }

}
