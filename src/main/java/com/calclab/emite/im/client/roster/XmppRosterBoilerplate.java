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

package com.calclab.emite.im.client.roster;

import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.calclab.emite.im.client.roster.events.RosterGroupChangedEvent;
import com.calclab.emite.im.client.roster.events.RosterGroupChangedHandler;
import com.calclab.emite.im.client.roster.events.RosterItemChangedEvent;
import com.calclab.emite.im.client.roster.events.RosterItemChangedHandler;
import com.calclab.emite.im.client.roster.events.RosterRetrievedEvent;
import com.calclab.emite.im.client.roster.events.RosterRetrievedHandler;
import com.google.gwt.event.shared.HandlerRegistration;

public abstract class XmppRosterBoilerplate implements XmppRoster {

	protected final XmppSession session;
	protected EmiteEventBus eventBus;
	protected boolean rosterReady;

	public XmppRosterBoilerplate(final XmppSession session) {
		this.session = session;
		eventBus = session.getEventBus();
		rosterReady = false;
	}

	@Override
	public HandlerRegistration addRosterGroupChangedHandler(final RosterGroupChangedHandler handler) {
		return RosterGroupChangedEvent.bind(eventBus, handler);
	}

	@Override
	public HandlerRegistration addRosterItemChangedHandler(final RosterItemChangedHandler handler) {
		return RosterItemChangedEvent.bind(eventBus, handler);
	}

	@Override
	public HandlerRegistration addRosterRetrievedHandler(final RosterRetrievedHandler handler) {
		return RosterRetrievedEvent.bind(eventBus, handler);
	}

	@Override
	public boolean isRosterReady() {
		return rosterReady;
	}

}
