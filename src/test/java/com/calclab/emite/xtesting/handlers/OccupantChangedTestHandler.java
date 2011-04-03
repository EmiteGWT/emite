package com.calclab.emite.xtesting.handlers;

import com.calclab.emite.xep.muc.client.events.OccupantChangedEvent;
import com.calclab.emite.xep.muc.client.events.OccupantChangedHandler;

public class OccupantChangedTestHandler extends TestHandler<OccupantChangedEvent> implements OccupantChangedHandler {

    private final String type;

    public OccupantChangedTestHandler() {
	this(null);
    }

    public OccupantChangedTestHandler(String type) {
	this.type = type;
    }

    @Override
    public void onOccupantChanged(OccupantChangedEvent event) {
	if (type == null || type.equals(event.getChangeType()))
	    addEvent(event);
    }

}
