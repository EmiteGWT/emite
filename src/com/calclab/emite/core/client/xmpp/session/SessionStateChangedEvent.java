package com.calclab.emite.core.client.xmpp.session;

import com.calclab.emite.core.client.events.StateChangedEvent;
import com.calclab.emite.core.client.events.StateChangedHandler;

public class SessionStateChangedEvent extends StateChangedEvent {
    private static final Type<StateChangedHandler> TYPE = new Type<StateChangedHandler>();

    public static Type<StateChangedHandler> getType() {
	return TYPE;
    }

    public SessionStateChangedEvent(final String state) {
	super(TYPE, state);
    }

}
