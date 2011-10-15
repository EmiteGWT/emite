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

package com.calclab.emite.core.client.session;

import static com.google.common.base.Preconditions.checkNotNull;

import com.calclab.emite.core.client.events.SessionStatusChangedEvent;
import com.calclab.emite.core.client.stanzas.Presence;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * A simple component that sets the session ready after logged in. This
 * component is removed if InstantMessagingModule used.
 */
public class SessionReady implements SessionStatusChangedEvent.Handler {

	private final XmppSession session;
	private final HandlerRegistration handler;

	@Inject
	public SessionReady(final XmppSession session) {
		this.session = checkNotNull(session);

		handler = session.addSessionStatusChangedHandler(this, true);
	}

	@Override
	public void onSessionStatusChanged(final SessionStatusChangedEvent event) {
		if (event.is(SessionStatus.loggedIn)) {
			session.send(new Presence());
			session.setStatus(SessionStatus.ready);
		}
	}

	public void disable() {
		handler.removeHandler();
	}

}
