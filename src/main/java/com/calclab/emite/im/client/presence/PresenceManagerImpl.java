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

import java.util.logging.Logger;

import com.calclab.emite.core.client.events.ErrorEvent;
import com.calclab.emite.core.client.events.PresenceEvent;
import com.calclab.emite.core.client.events.PresenceHandler;
import com.calclab.emite.core.client.events.StateChangedEvent;
import com.calclab.emite.core.client.events.StateChangedHandler;
import com.calclab.emite.core.client.xmpp.session.SessionReady;
import com.calclab.emite.core.client.xmpp.session.SessionStates;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.calclab.emite.core.client.xmpp.stanzas.Presence;
import com.calclab.emite.core.client.xmpp.stanzas.Presence.Type;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.presence.events.OwnPresenceChangedEvent;
import com.calclab.emite.im.client.presence.events.OwnPresenceChangedHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @see PresenceManager
 */
@Singleton
public class PresenceManagerImpl implements PresenceManager {
	
	private static final Logger logger = Logger.getLogger(PresenceManagerImpl.class.getName());
	
	private Presence ownPresence;
	static final Presence INITIAL_PRESENCE = new Presence(Type.unavailable, null, null);
	private final XmppSession session;

	@Inject
	public PresenceManagerImpl(final XmppSession session, final SessionReady sessionReady) {
		sessionReady.setEnabled(false);
		this.session = session;
		setOwnPresence(INITIAL_PRESENCE);

		// Upon connecting to the server and becoming an active resource, a
		// client SHOULD request the roster before sending initial presence
		session.addSessionStateChangedHandler(true, new StateChangedHandler() {
			@Override
			public void onStateChanged(final StateChangedEvent event) {
				if (event.is(SessionStates.rosterReady)) {
					logger.fine("Sending initial presence");
					final Presence ownPresence = getOwnPresence();
					final Presence initialPresence = ownPresence != INITIAL_PRESENCE ? ownPresence : new Presence(session.getCurrentUserURI());
					session.send(initialPresence);
					setOwnPresence(initialPresence);
					session.setSessionState(SessionStates.ready);
				} else if (event.is(SessionStates.loggingOut)) {
					sendUnavailablePresence(session.getCurrentUserURI());
				} else if (event.is(SessionStates.disconnected)) {
					setOwnPresence(INITIAL_PRESENCE);
				}
			}
		});

		session.addPresenceReceivedHandler(new PresenceHandler() {
			@Override
			public void onPresence(final PresenceEvent event) {
				final Presence presence = event.getPresence();
				final Type type = presence.getType();
				if (type == Type.probe) {
					session.send(getOwnPresence());
				} else if (type == Type.error) {
					session.getEventBus().fireEvent(new ErrorEvent("presenceError", "we received an error presence", presence));
				}
			}
		});

	}

	@Override
	public HandlerRegistration addOwnPresenceChangedHandler(final OwnPresenceChangedHandler handler) {
		return OwnPresenceChangedEvent.bind(session.getEventBus(), handler);
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
		session.getEventBus().fireEvent(new OwnPresenceChangedEvent(oldPresence, presence));
	}

}
