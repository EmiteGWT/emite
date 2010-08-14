package com.calclab.emite.core.client.conn;

import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.core.client.packet.IPacket;
import com.google.gwt.event.shared.GwtEvent;

public class StanzaReceivedEvent extends GwtEvent<StanzaReceivedHandler> {

    private static final Type<StanzaReceivedHandler> TYPE = new Type<StanzaReceivedHandler>();

    public static void bind(final EmiteEventBus eventBus, final StanzaReceivedHandler handler) {
	eventBus.addHandler(TYPE, handler);
    }

    public static Type<StanzaReceivedHandler> getType() {
	return TYPE;
    }

    private final IPacket stanza;

    public StanzaReceivedEvent(final IPacket stanza) {
	this.stanza = stanza;
    }

    @Override
    public Type<StanzaReceivedHandler> getAssociatedType() {
	return TYPE;
    }

    public IPacket getStanza() {
	return stanza;
    }

    @Override
    public String toDebugString() {
	return super.toDebugString() + stanza;
    }

    @Override
    protected void dispatch(final StanzaReceivedHandler handler) {
	handler.onStanzaReceived(this);
    }

}
