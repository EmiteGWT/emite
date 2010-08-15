package com.calclab.emite.core.client.conn;

import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.core.client.packet.IPacket;
import com.google.gwt.event.shared.HandlerRegistration;

public class StanzaSentEvent extends StanzaEvent {

    private static final Type<StanzaHandler> TYPE = new Type<StanzaHandler>();

    public static HandlerRegistration bind(EmiteEventBus eventBus, StanzaHandler handler) {
	return eventBus.addHandler(TYPE, handler);
    }

    public StanzaSentEvent(final IPacket stanza) {
	super(TYPE, stanza);
    }

}
