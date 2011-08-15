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

package com.calclab.emite.core.client.events;

import com.google.gwt.event.shared.GwtEvent;

/**
 * A state changed event. The state should be a string.
 * 
 * @author dani
 * 
 */
public abstract class StateChangedEvent extends GwtEvent<StateChangedHandler> {

	private final String state;
	private final Type<StateChangedHandler> type;

	/**
	 * Craete a state changed event.
	 * 
	 * @param type
	 * @param state
	 */
	protected StateChangedEvent(final Type<StateChangedHandler> type, final String state) {
		assert type != null : "Type in StateChanged can't be null";
		assert state != null : "State in StateChanged can't be null";
		this.type = type;
		this.state = state;
	}

	@Override
	public Type<StateChangedHandler> getAssociatedType() {
		return type;
	}

	/**
	 * Retrieve the event state
	 * 
	 * @return event's state
	 */
	public String getState() {
		return state;
	}

	/**
	 * Test if the following state is the event's state
	 * 
	 * @param state
	 * @return true if state is the event state
	 */
	public boolean is(final String state) {
		return this.state.equals(state);
	}

	@Override
	public String toDebugString() {
		return super.toDebugString() + state;
	}

	@Override
	protected void dispatch(final StateChangedHandler handler) {
		handler.onStateChanged(this);
	}

}
