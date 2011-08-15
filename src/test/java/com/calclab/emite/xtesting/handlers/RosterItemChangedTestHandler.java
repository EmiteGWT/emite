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

package com.calclab.emite.xtesting.handlers;

import com.calclab.emite.im.client.roster.RosterItem;
import com.calclab.emite.im.client.roster.events.RosterItemChangedEvent;
import com.calclab.emite.im.client.roster.events.RosterItemChangedHandler;

public class RosterItemChangedTestHandler extends TestHandler<RosterItemChangedEvent> implements RosterItemChangedHandler {

	private final String type;

	public RosterItemChangedTestHandler() {
		this(null);
	}

	public RosterItemChangedTestHandler(final String type) {
		this.type = type;
	}

	public RosterItem getLastRosterItem() {
		return hasEvent() ? getLastEvent().getRosterItem() : null;
	}

	@Override
	public void onRosterItemChanged(final RosterItemChangedEvent event) {
		if (type == null || type.equals(event.getChangeType())) {
			addEvent(event);
		}
	}

}
