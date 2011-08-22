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

package com.calclab.emite.core.client.conn;

import com.google.web.bindery.event.shared.Event;

public class ConnectionStateChangedEvent extends Event<ConnectionStateChangedEvent.Handler> {
	
	public interface Handler {
		void onConnectionStateChanged(ConnectionStateChangedEvent event);
	}
	
	public static final Type<Handler> TYPE = new Type<Handler>();

	private final ConnectionState state;
	private final String description;
	private final int value;

	protected ConnectionStateChangedEvent(final ConnectionState state) {
		this(state, null, 0);
	}

	protected ConnectionStateChangedEvent(final ConnectionState state, final String description) {
		this(state, description, 0);
	}

	protected ConnectionStateChangedEvent(final ConnectionState state, final String description, final int count) {
		assert state != null : "state can't be null in ConnectionStateEvents";
		this.state = state;
		this.description = description;
		value = count;
	}

	public ConnectionState getState() {
		return state;
	}
	
	public String getDescription() {
		return description;
	}

	public int getValue() {
		return value;
	}

	public boolean is(final ConnectionState state) {
		return this.state.equals(state);
	}

	@Override
	public String toDebugString() {
		final String desc = description != null ? "(" + description + ")" : "";
		return super.toDebugString() + " " + state + desc + " value: " + value;
	}
	
	@Override
	public Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(final Handler handler) {
		handler.onConnectionStateChanged(this);
	}

}
