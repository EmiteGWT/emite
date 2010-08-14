package com.calclab.emite.core.client.events;

import com.google.gwt.event.shared.GwtEvent;

public abstract class StateChangedEvent extends GwtEvent<StateChangedHandler> {

    private final String state;
    private final Type<StateChangedHandler> type;

    protected StateChangedEvent(final Type<StateChangedHandler> type, final String state) {
	assert type != null : "Type in StateChanged can't be null";
	assert state != null : "State in StateChanged can't be null";
	this.type = type;
	this.state = state;
    }

    @Override
    public Type<StateChangedHandler> getAssociatedType() {
	return type;
    }

    public String getState() {
	return state;
    }

    public boolean is(final String state) {
	return this.state.equals(state);
    }

    @Override
    public String toDebugString() {
	return super.toDebugString() + state;
    }

    @Override
    protected void dispatch(final StateChangedHandler handler) {
	handler.onStateChanged(this);
    }

}
