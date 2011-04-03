package com.calclab.emite.xep.storage.client.events;

import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.core.client.xmpp.stanzas.IQ;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class PrivateStorageResponseEvent extends GwtEvent<PrivateStorageResponseHandler> {

    private static final Type<PrivateStorageResponseHandler> TYPE = new Type<PrivateStorageResponseHandler>();

    public static HandlerRegistration bind(EmiteEventBus eventBus, PrivateStorageResponseHandler handler) {
	return eventBus.addHandler(TYPE, handler);
    }

    private final IQ response;

    public PrivateStorageResponseEvent(IQ response) {
	this.response = response;
    }

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<PrivateStorageResponseHandler> getAssociatedType() {
	return TYPE;
    }

    @Override
    protected void dispatch(PrivateStorageResponseHandler handler) {
	handler.onStorageResponse(this);
    }

    public IQ getResponseIQ() {
	return response;
    }

}
