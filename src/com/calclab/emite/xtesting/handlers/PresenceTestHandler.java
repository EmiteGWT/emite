package com.calclab.emite.xtesting.handlers;

import com.calclab.emite.core.client.events.PresenceEvent;
import com.calclab.emite.core.client.events.PresenceHandler;
import com.calclab.emite.core.client.xmpp.stanzas.Presence;

public class PresenceTestHandler extends TestHandler<PresenceEvent> implements PresenceHandler {

    public Presence getLastPresence() {
	return hasEvent() ? getLastEvent().getPresence() : null;
    }

    @Override
    public void onPresence(final PresenceEvent event) {
	setEvent(event);
    }
}
