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

/**
 * EventBuses are created using this factory. This allow us to change the
 * default event bus (for example: different loggin or testing).
 * 
 * The default implementation (GwtEventBus) doesn't work outside GWT
 * enviroments, so it should be replaced when unit testing
 * 
 */
public class EventBusFactory {

	/**
	 * A simple interface to create event buses
	 */
	public static interface Factory {
		public EmiteEventBus create(String eventBusName);
	}

	/**
	 * The default factory creates a Gwt event bus.
	 */
	private static Factory factory = new Factory() {
		@Override
		public EmiteEventBus create(final String eventBusName) {
			return new GwtEmiteEventBus(eventBusName);
		}
	};

	/**
	 * Create an event bus with the given name
	 * 
	 * @param eventBusName
	 *            the bus name: using for logging
	 * @return a new event bus
	 */
	public static EmiteEventBus create(final String eventBusName) {
		return EventBusFactory.factory.create(eventBusName);
	}

	/**
	 * Changes the event bus factory
	 * 
	 * @param factory
	 */
	public static void setFactory(final Factory factory) {
		EventBusFactory.factory = factory;
	}

}
