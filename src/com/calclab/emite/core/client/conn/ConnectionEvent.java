package com.calclab.emite.core.client.conn;

import com.google.gwt.event.shared.GwtEvent;

public class ConnectionEvent extends GwtEvent<ConnectionHandler> {
    public static enum EventType {
	/**
	 * The connection is now connected
	 */
	connected,
	/**
	 * The connection is now desconnected
	 */
	disconnected,
	/**
	 * The connection received an error
	 */
	error,
	/**
	 * The connection received a valid response
	 */
	response,
	/**
	 * The connection is going to (re)try connect a lost connection
	 */
	beforeRetry
    }

    private static final Type<ConnectionHandler> TYPE = new Type<ConnectionHandler>();

    public static Type<ConnectionHandler> getType() {
	return TYPE;
    }

    private final EventType eventType;
    private final String text;
    private final int count;

    public ConnectionEvent(final EventType eventType) {
	this(eventType, null, 0);
    }

    public ConnectionEvent(final EventType eventType, final String text) {
	this(eventType, text, 0);
    }

    public ConnectionEvent(final EventType eventType, final String text, final int count) {
	assert eventType != null : "EventType can't be null in ConnectionEvents";
	this.count = count;
	this.text = text;
	this.eventType = eventType;
    }

    @Override
    public Type<ConnectionHandler> getAssociatedType() {
	return TYPE;
    }

    public int getCount() {
	return count;
    }

    public String getText() {
	return text;
    }

    public boolean is(final EventType connected) {
	return eventType.equals(connected);
    }

    @Override
    public String toDebugString() {
	final String value = text != null ? "(" + text + ")" : "";
	return super.toDebugString() + " " + eventType + value;
    }

    @Override
    protected void dispatch(final ConnectionHandler handler) {
	handler.onStateChanged(this);
    }

}
