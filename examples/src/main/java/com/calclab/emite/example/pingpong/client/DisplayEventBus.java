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

package com.calclab.emite.example.pingpong.client;

import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.example.pingpong.client.PingPongDisplay.Style;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;

public class DisplayEventBus extends HandlerManager implements EmiteEventBus {

	private final PingPongDisplay display;
	private final String eventBusName;

	public DisplayEventBus(final String eventBusName, final PingPongDisplay display) {
		super(null);
		this.eventBusName = eventBusName;
		this.display = display;
		display.print("New event bus: " + eventBusName, Style.eventBus);
	}

	@Override
	public void fireEvent(final GwtEvent<?> event) {
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				display.print("[" + eventBusName + "] " + event.toDebugString(), Style.eventBus);
				DisplayEventBus.super.fireEvent(event);
			}
		});
	}

}
