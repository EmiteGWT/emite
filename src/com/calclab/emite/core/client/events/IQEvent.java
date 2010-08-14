package com.calclab.emite.core.client.events;

import com.calclab.emite.core.client.xmpp.stanzas.IQ;
import com.google.gwt.event.shared.GwtEvent;

public class IQEvent extends GwtEvent<IQHandler> {

    private static final Type<IQHandler> TYPE = new Type<IQHandler>();

    public static Type<IQHandler> getType() {
	return TYPE;
    }

    private final IQ iq;

    public IQEvent(final IQ iq) {
	this.iq = iq;
    }

    @Override
    public Type<IQHandler> getAssociatedType() {
	return getType();
    }

    public IQ getIQ() {
	return iq;
    }

    @Override
    protected void dispatch(final IQHandler handler) {
	handler.onPacket(this);
    }

}
