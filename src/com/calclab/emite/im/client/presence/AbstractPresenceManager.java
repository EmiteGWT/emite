package com.calclab.emite.im.client.presence;

import com.calclab.emite.core.client.xmpp.stanzas.Presence;
import com.calclab.suco.client.events.Event;
import com.calclab.suco.client.events.Listener;

public abstract class AbstractPresenceManager implements PresenceManager {
    private Presence ownPresence;
    private final Event<Presence> onOwnPresenceChanged;

    public AbstractPresenceManager() {
	this.onOwnPresenceChanged = new Event<Presence>("presenceManager:onOwnPresenceChanged");
    }

    /**
     * Return the current logged in user presence or a Presence with type
     * unavailable if logged out
     * 
     * @return
     */
    public Presence getOwnPresence() {
	return ownPresence;
    }

    public void onOwnPresenceChanged(final Listener<Presence> listener) {
	onOwnPresenceChanged.add(listener);
    }

    public void setOwnPresence(Presence presence) {
	ownPresence = presence;
	onOwnPresenceChanged.fire(ownPresence);
    }

}
