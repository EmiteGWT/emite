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

package com.calclab.emite.im.client.chat.events;

import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.core.client.events.StateChangedEvent;
import com.calclab.emite.core.client.events.StateChangedHandler;
import com.google.gwt.event.shared.HandlerRegistration;

public class ChatStateChangedEvent extends StateChangedEvent {

	private static final Type<StateChangedHandler> TYPE = new Type<StateChangedHandler>();

	public static HandlerRegistration bind(final EmiteEventBus eventBus, final StateChangedHandler handler) {
		return eventBus.addHandler(TYPE, handler);
	}

	public ChatStateChangedEvent(final String state) {
		super(TYPE, state);
	}

}
