package com.calclab.emite.xtesting.handlers;

import com.calclab.emite.im.client.roster.events.RosterItemChangedEvent;
import com.calclab.emite.im.client.roster.events.RosterItemChangedHandler;

public class RosterItemChangedTestHandler extends TestHandler<RosterItemChangedEvent> implements
	RosterItemChangedHandler {

    @Override
    public void onRosterItemChanged(RosterItemChangedEvent event) {
	addEvent(event);
    }

}
