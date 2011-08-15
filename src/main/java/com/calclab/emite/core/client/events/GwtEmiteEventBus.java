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

import java.util.logging.Logger;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;

public class GwtEmiteEventBus extends HandlerManager implements EmiteEventBus {

	private static final Logger logger = Logger.getLogger(GwtEmiteEventBus.class.getName());
	
	private final String eventBusName;

	GwtEmiteEventBus(final String eventBusName) {
		super(null);
		this.eventBusName = eventBusName;
		logger.info("New EventBus: " + eventBusName);
	}

	@Override
	public void fireEvent(final GwtEvent<?> event) {
		logger.finer("[" + eventBusName + "] " + event.toDebugString());
		super.fireEvent(event);
	}

}
