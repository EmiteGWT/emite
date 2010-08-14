package com.calclab.emite.xtesting.handlers;

import com.calclab.emite.core.client.events.StateChangedEvent;
import com.calclab.emite.core.client.events.StateChangedHandler;

public class StateChangedTestHandler extends TestHandler<StateChangedEvent> implements StateChangedHandler {
    public String getState() {
	return hasEvent() ? event.getState() : null;
    }

    @Override
    public void onStateChanged(final StateChangedEvent event) {
	this.event = event;
    }

}
