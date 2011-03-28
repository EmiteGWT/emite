package com.calclab.emite.xtesting.handlers;

import com.calclab.emite.xep.muc.client.events.OccupantChangedEvent;
import com.calclab.emite.xep.muc.client.events.OccupantChangedHandler;

public class OccupantChangedTestHandler extends
		TestHandler<OccupantChangedEvent> implements OccupantChangedHandler {

	@Override
	public void onOccupantChanged(OccupantChangedEvent event) {
		addEvent(event);
	}

}
