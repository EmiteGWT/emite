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

public class Event<T> {
    private ArrayList<Listener<T>> listeners;
    private final String id;
    private ArrayList<Listener<T>> toAdd;
    private ArrayList<Listener<T>> toRemove;

    public Event(final String id) {
	this.id = id;
	listeners = null;
	toAdd = null;
    }

    public void add(final Listener<T> listener) {
	if (toAdd == null) {
	    toAdd = new ArrayList<Listener<T>>();
	}
	toAdd.add(listener);
    }

    public void fire(final T event) {
	GWT.log("Signal " + id + " fired with event: " + event + " (" + (listeners != null ? listeners.size() : 0)
		+ " times)", null);
	addPending();
	removePending();
	fire(event, listeners);
	fire(event, toAdd);
	addPending();
	removePending();
    }

    public void remove(final Listener<T> listener) {
	if (toRemove == null) {
	    toRemove = new ArrayList<Listener<T>>();
	}
	toRemove.add(listener);
    }

    private void addPending() {
	if (toAdd != null) {
	    if (listeners == null) {
		this.listeners = new ArrayList<Listener<T>>();
	    }
	    listeners.addAll(toAdd);
	    toAdd = null;
	}
    }

    private void fire(final T event, final ArrayList<Listener<T>> listenerList) {
	if (listenerList != null) {
	    for (final Listener<T> listener : listenerList) {
		listener.onEvent(event);
	    }
	}
    }

    private void removePending() {
	if (toRemove != null) {
	    if (listeners != null) {
		listeners.removeAll(toRemove);
	    }
	    toRemove = null;
	}
    }
}
