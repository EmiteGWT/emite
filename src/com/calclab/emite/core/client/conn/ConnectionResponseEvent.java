package com.calclab.emite.core.client.conn;

import com.calclab.emite.core.client.events.EmiteEventBus;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class ConnectionResponseEvent extends GwtEvent<ConnectionResponseHandler> {

    private static final Type<ConnectionResponseHandler> TYPE = new Type<ConnectionResponseHandler>();

    public static HandlerRegistration bind(final EmiteEventBus eventBus, final ConnectionResponseHandler handler) {
	return eventBus.addHandler(TYPE, handler);
    }

    private final String response;

    public ConnectionResponseEvent(final String response) {
	this.response = response;
    }

    @Override
    public Type<ConnectionResponseHandler> getAssociatedType() {
	return TYPE;
    }

    public String getResponse() {
	return response;
    }

    @Override
    public String toDebugString() {
	return super.toDebugString() + response;
    }

    @Override
    protected void dispatch(final ConnectionResponseHandler handler) {
	handler.onResponse(this);
    }

}
