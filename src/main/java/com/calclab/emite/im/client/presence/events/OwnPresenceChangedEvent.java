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

package com.calclab.emite.im.client.presence.events;

import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.core.client.xmpp.stanzas.Presence;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * The current user presence has changed.
 * 
 */
public class OwnPresenceChangedEvent extends GwtEvent<OwnPresenceChangedHandler> {

	private static final Type<OwnPresenceChangedHandler> TYPE = new Type<OwnPresenceChangedHandler>();

	public static HandlerRegistration bind(final EmiteEventBus eventBus, final OwnPresenceChangedHandler handler) {
		return eventBus.addHandler(TYPE, handler);
	}

	private final Presence currentPresence;

	private final Presence oldPresence;

	public OwnPresenceChangedEvent(final Presence oldPresence, final Presence currentPresence) {
		this.oldPresence = oldPresence;
		this.currentPresence = currentPresence;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<OwnPresenceChangedHandler> getAssociatedType() {
		return TYPE;
	}

	public Presence getCurrentPresence() {
		return currentPresence;
	}

	public Presence getOldPresence() {
		return oldPresence;
	}

	@Override
	public String toDebugString() {
		return super.toDebugString() + currentPresence.toString();
	}

	@Override
	protected void dispatch(final OwnPresenceChangedHandler handler) {
		handler.onOwnPresenceChanged(this);
	}

}
