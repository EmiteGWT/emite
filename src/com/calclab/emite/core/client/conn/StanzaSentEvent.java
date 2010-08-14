package com.calclab.emite.core.client.conn;

import com.calclab.emite.core.client.packet.IPacket;
import com.google.gwt.event.shared.GwtEvent;

public class StanzaSentEvent extends GwtEvent<StanzaSentHandler> {

    private static final Type<StanzaSentHandler> TYPE = new Type<StanzaSentHandler>();

    public static Type<StanzaSentHandler> getType() {
	return TYPE;
    }

    private final IPacket stanza;

    public StanzaSentEvent(final IPacket stanza) {
	this.stanza = stanza;
    }

    @Override
    public Type<StanzaSentHandler> getAssociatedType() {
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
    protected void dispatch(final StanzaSentHandler handler) {
	handler.onStanzaSent(this);
    }

}
