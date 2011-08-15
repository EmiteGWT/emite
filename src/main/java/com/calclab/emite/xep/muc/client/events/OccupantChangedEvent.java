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

package com.calclab.emite.xep.muc.client.events;

import com.calclab.emite.core.client.events.ChangedEvent;
import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.xep.muc.client.Occupant;
import com.google.gwt.event.shared.HandlerRegistration;

public class OccupantChangedEvent extends ChangedEvent<OccupantChangedHandler> {

	private static final Type<OccupantChangedHandler> TYPE = new Type<OccupantChangedHandler>();

	public static HandlerRegistration bind(final EmiteEventBus eventBus, final OccupantChangedHandler handler) {
		return eventBus.addHandler(TYPE, handler);
	}

	private final Occupant occupant;

	public OccupantChangedEvent(final String changeType, final Occupant occupant) {
		super(TYPE, changeType);
		this.occupant = occupant;
	}

	public Occupant getOccupant() {
		return occupant;
	}

	@Override
	public String toDebugString() {
		return super.toDebugString() + occupant;
	}

	@Override
	protected void dispatch(final OccupantChangedHandler handler) {
		handler.onOccupantChanged(this);
	}

}
