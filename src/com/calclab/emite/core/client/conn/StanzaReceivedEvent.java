package com.calclab.emite.core.client.conn;

import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.core.client.packet.IPacket;
import com.google.gwt.event.shared.HandlerRegistration;

public class StanzaReceivedEvent extends StanzaEvent {

    private static final Type<StanzaHandler> TYPE = new Type<StanzaHandler>();

    public static HandlerRegistration bind(final EmiteEventBus eventBus, final StanzaHandler handler) {
	return eventBus.addHandler(TYPE, handler);
    }

    public StanzaReceivedEvent(final IPacket stanza) {
	super(TYPE, stanza);
    }

}
