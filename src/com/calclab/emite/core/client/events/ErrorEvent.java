package com.calclab.emite.core.client.events;

import com.calclab.emite.core.client.packet.IPacket;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class ErrorEvent extends GwtEvent<ErrorHandler> {

    private static final Type<ErrorHandler> TYPE = new Type<ErrorHandler>();

    public static HandlerRegistration bind(EmiteEventBus eventBus, ErrorHandler handler) {
	return eventBus.addHandler(TYPE, handler);
    }
    private final String errorType;
    private final String description;
    private final IPacket stanza;

    public ErrorEvent(String errorType, String description, IPacket stanza) {
	this.errorType = errorType;
	this.description = description;
	this.stanza = stanza;
    }

    @Override
    public Type<ErrorHandler> getAssociatedType() {
	return TYPE;
    }

    public String getDescription() {
	return description;
    }

    public String getErrorType() {
	return errorType;
    }

    public IPacket getStanza() {
	return stanza;
    }

    @Override
    protected void dispatch(ErrorHandler handler) {
	handler.onError(this);
    }

}
