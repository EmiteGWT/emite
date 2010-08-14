package com.calclab.emite.core.client.events;

import com.calclab.emite.core.client.xmpp.stanzas.Presence;

public class PresenceReceivedEvent extends PresenceEvent {

    private static final Type<PresenceHandler> TYPE = new Type<PresenceHandler>();

    public static Type<PresenceHandler> getType() {
	return TYPE;
    }

    public PresenceReceivedEvent(final Presence presence) {
	super(TYPE, presence);
    }

}
