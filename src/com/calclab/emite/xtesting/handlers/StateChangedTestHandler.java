package com.calclab.emite.xtesting.handlers;

import com.calclab.emite.core.client.events.StateChangedEvent;
import com.calclab.emite.core.client.events.StateChangedHandler;

public class StateChangedTestHandler extends TestHandler<StateChangedEvent> implements StateChangedHandler {
    public String getLastState() {
	return hasEvent() ? getLastEvent().getState() : null;
    }

    @Override
    public void onStateChanged(final StateChangedEvent event) {
	addEvent(event);
    }

}
