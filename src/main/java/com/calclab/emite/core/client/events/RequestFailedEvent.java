package com.calclab.emite.core.client.events;

import com.calclab.emite.core.client.xmpp.stanzas.IQ;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * A request (IQ request) has failed
 */
public class RequestFailedEvent extends GwtEvent<RequestFailedHandler> {

    private static final Type<RequestFailedHandler> TYPE = new Type<RequestFailedHandler>();

    public static HandlerRegistration bind(EmiteEventBus eventBus, RequestFailedHandler handler) {
	return eventBus.addHandler(TYPE, handler);
    }
    private final String requestType;
    private final String description;

    private final IQ iq;

    public RequestFailedEvent(String requestType, String description, IQ iq) {
	this.requestType = requestType;
	this.description = description;
	this.iq = iq;
    }

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<RequestFailedHandler> getAssociatedType() {
	return TYPE;
    }

    public String getDescription() {
	return description;
    }

    public IQ getIq() {
	return iq;
    }

    public String getRequestType() {
	return requestType;
    }

    @Override
    protected void dispatch(RequestFailedHandler handler) {
	handler.onRequestFailed(this);
    }

}
