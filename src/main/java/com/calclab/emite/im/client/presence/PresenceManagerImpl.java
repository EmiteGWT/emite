/*
 * ((e)) emite: A pure Google Web Toolkit XMPP library
 * Copyright (c) 2008-2011 The Emite development team
 * 
 * This file is part of Emite.
 *
 * Emite is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * Emite is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with Emite.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.calclab.emite.im.client.presence;

import com.calclab.emite.core.client.events.ErrorEvent;
import com.calclab.emite.core.client.events.PresenceReceivedEvent;
import com.calclab.emite.core.client.xmpp.session.SessionReady;
import com.calclab.emite.core.client.xmpp.session.SessionState;
import com.calclab.emite.core.client.xmpp.session.SessionStateChangedEvent;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.calclab.emite.core.client.xmpp.stanzas.Presence;
import com.calclab.emite.core.client.xmpp.stanzas.Presence.Type;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * @see PresenceManager
 */
@Singleton
public class PresenceManagerImpl implements PresenceManager, SessionStateChangedEvent.Handler, PresenceReceivedEvent.Handler {

	static final Presence INITIAL_PRESENCE = new Presence(Type.unavailable, null, null);

	private final EventBus eventBus;
	private final XmppSession session;

	private Presence ownPresence;

	@Inject
	public PresenceManagerImpl(@Named("emite") final EventBus eventBus, final XmppSession session, final SessionReady sessionReady) {
		this.eventBus = eventBus;
		this.session = session;
		ownPresence = INITIAL_PRESENCE;
		
		sessionReady.setEnabled(false);

		// Upon connecting to the server and becoming an active resource, a
		// client SHOULD request the roster before sending initial presence
		session.addSessionStateChangedHandler(false, this);
		session.addPresenceReceivedHandler(this);
	}

	@Override
	public void onSessionStateChanged(final SessionStateChangedEvent event) {
		if (event.is(SessionState.rosterReady)) {
			final Presence initialPresence = ownPresence != INITIAL_PRESENCE ? ownPresence : new Presence(session.getCurrentUserURI());
			session.send(initialPresence);
			setOwnPresence(initialPresence);
			session.setSessionState(SessionState.ready);
		} else if (event.is(SessionState.loggingOut)) {
			sendUnavailablePresence(session.getCurrentUserURI());
		} else if (event.is(SessionState.disconnected)) {
			setOwnPresence(INITIAL_PRESENCE);
		}
	}

	@Override
	public void onPresenceReceived(final PresenceReceivedEvent event) {
		final Presence presence = event.getPresence();
		final Type type = presence.getType();
		if (type == Type.probe) {
			session.send(ownPresence);
		} else if (type == Type.error) {
			eventBus.fireEventFromSource(new ErrorEvent("presenceError", "we received an error presence", presence), this);
		}
	}

	@Override
	public HandlerRegistration addOwnPresenceChangedHandler(final OwnPresenceChangedEvent.Handler handler) {
		return eventBus.addHandlerToSource(OwnPresenceChangedEvent.TYPE, this, handler);
	}

	/**
	 * Set the logged in user's presence. If the user is not logged in, the
	 * presence is sent just after the initial presence
	 * 
	 * @see http://www.xmpp.org/rfcs/rfc3921.html#presence
	 * 
	 * @param presence
	 */
	@Override
	public void changeOwnPresence(final Presence presence) {
		session.send(presence);
		setOwnPresence(presence);
	}

	@Override
	public Presence getOwnPresence() {
		return ownPresence;
	}

	/**
	 * 5.1.5. Unavailable Presence (rfc 3921)
	 * 
	 * Before ending its session with a server, a client SHOULD gracefully
	 * become unavailable by sending a final presence stanza that possesses no
	 * 'to' attribute and that possesses a 'type' attribute whose value is
	 * "unavailable" (optionally, the final presence stanza MAY contain one or
	 * more <status/> elements specifying the reason why the user is no longer
	 * available).
	 * 
	 * @param userURI
	 */
	private void sendUnavailablePresence(final XmppURI userURI) {
		final Presence presence = new Presence(Type.unavailable, userURI, null);
		session.send(presence);
		setOwnPresence(presence);
	}

	private void setOwnPresence(final Presence presence) {
		final Presence oldPresence = ownPresence;
		ownPresence = presence;
		eventBus.fireEventFromSource(new OwnPresenceChangedEvent(oldPresence, presence), this);
	}

}
