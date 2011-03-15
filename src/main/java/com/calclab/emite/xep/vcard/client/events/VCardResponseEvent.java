package com.calclab.emite.xep.vcard.client.events;

import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.xep.vcard.client.VCardResponse;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class VCardResponseEvent extends GwtEvent<VCardResponseHandler> {

    private static final Type<VCardResponseHandler> TYPE = new Type<VCardResponseHandler>();

    public static HandlerRegistration bind(EmiteEventBus eventBus, VCardResponseHandler handler) {
	return eventBus.addHandler(TYPE, handler);
    }

    private final VCardResponse vCardResponse;

    public VCardResponseEvent(VCardResponse vCardResponse) {
	this.vCardResponse = vCardResponse;
    }

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<VCardResponseHandler> getAssociatedType() {
	return TYPE;
    }

    public VCardResponse getVCardResponse() {
	return vCardResponse;
    }

    @Override
    protected void dispatch(VCardResponseHandler handler) {
	handler.onVCardResponse(this);
    }

}
