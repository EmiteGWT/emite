package com.calclab.emite.core.client.events;

import com.calclab.emite.core.client.xmpp.stanzas.IQ;
import com.google.gwt.event.shared.HandlerRegistration;

public class IQReceivedEvent extends IQEvent {
    private static final Type<IQHandler> TYPE = new Type<IQHandler>();

    public static HandlerRegistration bind(EmiteEventBus eventBus, IQHandler handler) {
	return eventBus.addHandler(TYPE, handler);
    }

    public IQReceivedEvent(IQ iq) {
	super(TYPE, iq);
    }

}
