package com.calclab.emite.core.client.conn;

import com.calclab.emite.core.client.packet.IPacket;
import com.google.gwt.event.shared.GwtEvent;

public class StanzaEvent extends GwtEvent<StanzaHandler> {

    private final Type<StanzaHandler> type;
    private final IPacket stanza;

    public StanzaEvent(Type<StanzaHandler> type, IPacket stanza) {
	this.type = type;
	this.stanza = stanza;
    }

    @Override
    protected void dispatch(StanzaHandler handler) {
	handler.onStanza(this);
    }

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<StanzaHandler> getAssociatedType() {
	return type;
    }

    public IPacket getStanza() {
	return stanza;
    }

    @Override
    public String toDebugString() {
	return super.toDebugString() + stanza;
    }

}
