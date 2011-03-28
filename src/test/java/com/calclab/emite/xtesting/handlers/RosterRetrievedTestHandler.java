package com.calclab.emite.xtesting.handlers;

import java.util.Collection;

import com.calclab.emite.im.client.roster.RosterItem;
import com.calclab.emite.im.client.roster.events.RosterRetrievedEvent;
import com.calclab.emite.im.client.roster.events.RosterRetrievedHandler;

public class RosterRetrievedTestHandler extends
		TestHandler<RosterRetrievedEvent> implements RosterRetrievedHandler {

	public Collection<RosterItem> getLastRosterItems() {
		return hasEvent() ? getLastEvent().getRosterItems() : null;
	}

	@Override
	public void onRosterRetrieved(RosterRetrievedEvent event) {
		addEvent(event);
	}

}
