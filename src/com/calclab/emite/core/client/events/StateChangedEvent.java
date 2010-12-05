package com.calclab.emite.core.client.events;

import com.google.gwt.event.shared.GwtEvent;

/**
 * A state changed event. The state should be a string.
 * 
 * @author dani
 * 
 */
public abstract class StateChangedEvent extends GwtEvent<StateChangedHandler> {

    private final String state;
    private final Type<StateChangedHandler> type;

    /**
     * Craete a state changed event.
     * 
     * @param type
     * @param state
     */
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

    /**
     * Retrieve the event state
     * 
     * @return event's state
     */
    public String getState() {
        return state;
    }

    /**
     * Test if the following state is the event's state
     * 
     * @param state
     * @return true if state is the event state
     */
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
