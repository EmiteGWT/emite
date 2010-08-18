package com.calclab.emite.core.client.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class ErrorEvent extends GwtEvent<ErrorHandler> {

    private static final Type<ErrorHandler> TYPE = new Type<ErrorHandler>();

    public static HandlerRegistration bind(EmiteEventBus eventBus, ErrorHandler handler) {
	return eventBus.addHandler(TYPE, handler);
    }
    private final String errorType;
    private final String description;
    private final Object cause;

    public ErrorEvent(String errorType, String description, Object cause) {
	this.errorType = errorType;
	this.description = description;
	this.cause = cause;
    }

    @Override
    public Type<ErrorHandler> getAssociatedType() {
	return TYPE;
    }

    public Object getCause() {
	return cause;
    }

    public String getDescription() {
	return description;
    }

    public String getErrorType() {
	return errorType;
    }

    @Override
    protected void dispatch(ErrorHandler handler) {
	handler.onError(this);
    }

}