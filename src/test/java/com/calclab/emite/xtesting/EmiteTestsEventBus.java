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

package com.calclab.emite.xtesting;

import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.core.client.events.EventBusFactory;
import com.calclab.emite.core.client.events.EventBusFactory.Factory;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;

public class EmiteTestsEventBus extends HandlerManager implements EmiteEventBus {

	private static Factory factory;

	public static EmiteEventBus create(final String eventBusName) {
		if (EmiteTestsEventBus.factory == null) {
			EmiteTestsEventBus.factory = new Factory() {
				@Override
				public EmiteEventBus create(final String eventBusName) {
					return new EmiteTestsEventBus(eventBusName);
				}
			};
			EventBusFactory.setFactory(factory);
		}
		return EventBusFactory.create(eventBusName);
	}

	private final String name;

	public EmiteTestsEventBus(final String name) {
		super(null);
		this.name = name;
		System.out.println("New event bus: " + name);
	}

	@Override
	public void fireEvent(final GwtEvent<?> event) {
		System.out.println("[" + name + "] " + event.toDebugString());
		super.fireEvent(event);
	}

}
