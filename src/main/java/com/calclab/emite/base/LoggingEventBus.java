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

package com.calclab.emite.base;

import java.util.logging.Logger;

import com.google.web.bindery.event.shared.Event;
import com.google.web.bindery.event.shared.SimpleEventBus;

/**
 * An EventBus that logs all fired events.
 * 
 * FIXME: For testing only. To be removed.
 */
public class LoggingEventBus extends SimpleEventBus {

	private static final Logger logger = Logger.getLogger("EventBus");

	@Override
	public void fireEvent(final Event<?> event) {
		logger.finest("FIRE: " + event.toDebugString());
		super.fireEvent(event);
	}

	@Override
	public void fireEventFromSource(final Event<?> event, final Object source) {
		logger.finest("FIRE|" + source.toString() + ": " + event.toDebugString());
		super.fireEventFromSource(event, source);
	}
}
