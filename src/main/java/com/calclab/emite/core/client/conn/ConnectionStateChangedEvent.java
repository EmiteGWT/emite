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

import com.calclab.emite.core.client.events.EmiteEventBus;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class ConnectionStateChangedEvent extends GwtEvent<ConnectionStateChangedHandler> {
	public static class ConnectionState {
		/**
		 * The connection is now connected
		 */
		public static final String connected = "connected";
		/**
		 * The connection is now desconnected
		 */
		public static final String disconnected = "disconnected";
		/**
		 * The connection received an error
		 */
		public static final String error = "error";
		/**
		 * The connection will try to re-connect in the given milliseconds
		 */
		public static String waitingForRetry = "waitingForRetry";
	}

	private static final Type<ConnectionStateChangedHandler> TYPE = new Type<ConnectionStateChangedHandler>();

	public static HandlerRegistration bind(final EmiteEventBus eventBus, final ConnectionStateChangedHandler handler) {
		return eventBus.addHandler(TYPE, handler);
	}

	private final String state;
	private final String description;

	private final int value;

	public ConnectionStateChangedEvent(final String state) {
		this(state, null, 0);
	}

	public ConnectionStateChangedEvent(final String state, final String text) {
		this(state, text, 0);
	}

	public ConnectionStateChangedEvent(final String state, final String text, final int count) {
		assert state != null : "state can't be null in ConnectionStateEvents";
		value = count;
		description = text;
		this.state = state;
	}

	@Override
	public Type<ConnectionStateChangedHandler> getAssociatedType() {
		return TYPE;
	}

	public String getDescription() {
		return description;
	}

	public String getState() {
		return state;
	}

	public int getValue() {
		return value;
	}

	public boolean is(final String state) {
		return this.state.equals(state);
	}

	@Override
	public String toDebugString() {
		final String desc = description != null ? "(" + description + ")" : "";
		return super.toDebugString() + " " + state + desc + " value: " + value;
	}

	@Override
	protected void dispatch(final ConnectionStateChangedHandler handler) {
		handler.onStateChanged(this);
	}

}
