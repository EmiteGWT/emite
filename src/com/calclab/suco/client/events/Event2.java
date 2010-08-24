/*
 *
 * suco: Mini IoC framework a-la-guice style for GWT
 *
 * (c) 2009 The suco development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.calclab.suco.client.events;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;

@Deprecated
public class Event2<A, B> {
    private ArrayList<Listener2<A, B>> listeners;
    private final String id;

    public Event2(final String id) {
	this.id = id;
	listeners = null;
    }

    public void add(final Listener2<A, B> listener) {
	if (listeners == null) {
	    this.listeners = new ArrayList<Listener2<A, B>>();
	}
	listeners.add(listener);
    }

    public void fire(final A param1, final B param2) {
	if (listeners != null) {
	    GWT.log("Signal " + id + " fired with params [" + param1 + ", " + param2 + "]", null);
	    for (final Listener2<A, B> listener : listeners) {
		listener.onEvent(param1, param2);
	    }
	}
    }

    public void remove(final Listener2<A, B> listener) {
	if (listeners != null) {
	    listeners.remove(listener);
	}
    }
}
