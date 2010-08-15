package com.calclab.emite.im.client.chat.events;

import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.core.client.events.StateChangedEvent;
import com.calclab.emite.core.client.events.StateChangedHandler;
import com.google.gwt.event.shared.HandlerRegistration;

public class ChatStateChangedEvent extends StateChangedEvent {

    private static final Type<StateChangedHandler> TYPE = new Type<StateChangedHandler>();

    public static HandlerRegistration bind(final EmiteEventBus eventBus, final StateChangedHandler handler) {
	return eventBus.addHandler(TYPE, handler);
    }

    public ChatStateChangedEvent(final String state) {
	super(TYPE, state);
    }

}
