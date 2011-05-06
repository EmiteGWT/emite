package com.calclab.emite.xtesting.handlers;

import com.calclab.emite.core.client.xmpp.stanzas.Presence;
import com.calclab.emite.im.client.presence.events.OwnPresenceChangedEvent;
import com.calclab.emite.im.client.presence.events.OwnPresenceChangedHandler;

public class OwnPresenceChangedTestHandler extends TestHandler<OwnPresenceChangedEvent> implements OwnPresenceChangedHandler {

	public Presence getCurrentPresence() {
		return hasEvent() ? getLastEvent().getCurrentPresence() : null;
	}

	@Override
	public void onOwnPresenceChanged(final OwnPresenceChangedEvent event) {
		addEvent(event);
	}
}
