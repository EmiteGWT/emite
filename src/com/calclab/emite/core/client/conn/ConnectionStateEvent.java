package com.calclab.emite.core.client.conn;

import com.google.gwt.event.shared.GwtEvent;

public class ConnectionStateEvent extends GwtEvent<ConnectionStateHandler> {
    public static class ConnectionState {
	/**
	 * The connection is now connected
	 */
	public static final String connected = "connected";
	/**
	 * The connection is now desconnected
	 */
	public static final String disconnected = "disconnected";
	/**
	 * The connection received an error
	 */
	public static final String error = "error";
	/**
	 * The connection will try to re-connect in the given milliseconds
	 */
	public static String waitingForRetry = "waitingForRetry";
    }

    private static final Type<ConnectionStateHandler> TYPE = new Type<ConnectionStateHandler>();

    public static Type<ConnectionStateHandler> getType() {
	return TYPE;
    }

    private final String state;
    private final String description;
    private final int value;

    public ConnectionStateEvent(final String state) {
	this(state, null, 0);
    }

    public ConnectionStateEvent(final String state, final String text) {
	this(state, text, 0);
    }

    public ConnectionStateEvent(final String state, final String text, final int count) {
	assert state != null : "state can't be null in ConnectionStateEvents";
	this.value = count;
	this.description = text;
	this.state = state;
    }

    @Override
    public Type<ConnectionStateHandler> getAssociatedType() {
	return TYPE;
    }

    public int getValue() {
	return value;
    }

    public String getDescription() {
	return description;
    }

    public boolean is(final String state) {
	return this.state.equals(state);
    }

    @Override
    public String toDebugString() {
	final String desc = description != null ? "(" + description + ")" : "";
	return super.toDebugString() + " " + state + desc + " value: " + value;
    }

    @Override
    protected void dispatch(final ConnectionStateHandler handler) {
	handler.onStateChanged(this);
    }

}
