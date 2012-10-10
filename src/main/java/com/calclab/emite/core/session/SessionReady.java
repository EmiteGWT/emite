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

package com.calclab.emite.core.session;

import static com.google.common.base.Preconditions.checkNotNull;

import com.calclab.emite.core.events.SessionStatusChangedEvent;
import com.calclab.emite.core.stanzas.Presence;

/**
 * A simple component that sets the session ready after logging in.
 * 
 * This component is disabled if PresenceManager is used.
 */
public final class SessionReady implements SessionStatusChangedEvent.Handler {

	private static boolean enabled = true;
	
	/**
	 * Disable this component.
	 * 
	 * This method is meant to be called by PresenceManager.
	 */
	public static final void disable() {
		enabled = false;
	}
	
	private final XmppSessionImpl session;

	protected SessionReady(final XmppSessionImpl session) {
		this.session = checkNotNull(session);

		session.addSessionStatusChangedHandler(this, true);
	}

	@Override
	public void onSessionStatusChanged(final SessionStatusChangedEvent event) {
		if (enabled && SessionStatus.rosterReady.equals(event.getStatus())) {
			session.send(new Presence());
			session.setStatus(SessionStatus.ready);
		}
	}

}
