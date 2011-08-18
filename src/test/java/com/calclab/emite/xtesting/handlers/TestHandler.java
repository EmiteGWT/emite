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

package com.calclab.emite.xtesting.handlers;

import java.util.ArrayList;

public abstract class TestHandler<T> {
	private final ArrayList<T> events;

	public TestHandler() {
		events = new ArrayList<T>();
	}

	public int getCalledTimes() {
		return events.size();
	}

	public T getEvent(final int index) {
		return events.get(index);
	}

	public T getLastEvent() {
		final int size = getCalledTimes();
		return size > 0 ? events.get(size - 1) : null;
	}

	public boolean hasEvent() {
		return isCalled();
	}

	public boolean isCalledOnce() {
		return getCalledTimes() == 1;
	}

	public boolean isCalled() {
		return getCalledTimes() > 0;
	}

	public boolean isNotCalled() {
		return getCalledTimes() == 0;
	}

	public void addEvent(final T event) {
		events.add(event);
	}
}
