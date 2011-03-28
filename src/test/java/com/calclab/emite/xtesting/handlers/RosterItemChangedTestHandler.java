package com.calclab.emite.xtesting.handlers;

import com.calclab.emite.im.client.roster.RosterItem;
import com.calclab.emite.im.client.roster.events.RosterItemChangedEvent;
import com.calclab.emite.im.client.roster.events.RosterItemChangedHandler;

public class RosterItemChangedTestHandler extends
		TestHandler<RosterItemChangedEvent> implements RosterItemChangedHandler {

	private final String type;

	public RosterItemChangedTestHandler() {
		this(null);
	}

	public RosterItemChangedTestHandler(String type) {
		this.type = type;
	}

	public RosterItem getLastRosterItem() {
		return hasEvent() ? getLastEvent().getRosterItem() : null;
	}

	@Override
	public void onRosterItemChanged(RosterItemChangedEvent event) {
		if (type == null || type.equals(event.getChangeType()))
			addEvent(event);
	}

}
