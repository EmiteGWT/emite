package com.calclab.emite.core.client.events;

import com.calclab.emite.core.client.xmpp.stanzas.IQ;
import com.google.gwt.event.shared.GwtEvent;

public abstract class IQEvent extends GwtEvent<IQHandler> {

    private final IQ iq;
    private final Type<IQHandler> type;

    public IQEvent(Type<IQHandler> type, final IQ iq) {
	this.type = type;
	this.iq = iq;
    }

    @Override
    public Type<IQHandler> getAssociatedType() {
	return type;
    }

    public IQ getIQ() {
	return iq;
    }

    @Override
    public String toDebugString() {
	return super.toDebugString() + iq;
    }

    @Override
    protected void dispatch(final IQHandler handler) {
	handler.onPacket(this);
    }

}
