package com.calclab.emite.core.client.xmpp.session;

import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.core.client.events.StateChangedEvent;
import com.calclab.emite.core.client.events.StateChangedHandler;
import com.google.gwt.event.shared.HandlerRegistration;

public class SessionStateChangedEvent extends StateChangedEvent {
    private static final Type<StateChangedHandler> TYPE = new Type<StateChangedHandler>();

    public static HandlerRegistration bind(EmiteEventBus eventBus, StateChangedHandler handler) {
	return eventBus.addHandler(TYPE, handler);
    }

    public SessionStateChangedEvent(final String state) {
	super(TYPE, state);
    }

}
