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
package com.calclab.suco.testing.events;

import com.calclab.suco.client.events.Listener0;

public class MockedListener0 implements Listener0, VerificableListener {
    private int timesCalled;

    public MockedListener0() {
	timesCalled = 0;
    }

    public void clear() {
	timesCalled = 0;
    }

    public int getCalledTimes() {
	return timesCalled;
    }

    public boolean isCalled() {
	return getCalledTimes() > 0;
    }

    public boolean isCalled(final int timesCalled) {
	return getCalledTimes() == timesCalled;
    }

    public boolean isCalledOnce() {
	return getCalledTimes() == 1;
    }

    /**
     * does't have sense for Listener0. Throws RuntimeException
     */
    public boolean isCalledWithEquals(final Object... expectedOrderedValues) {
	throw new RuntimeException("Listener0 can't have parameters");
    }

    /**
     * does't have sense for Listener0. Throws RuntimeException
     */
    public boolean isCalledWithSame(final Object... expectedOrderedValues) {
	throw new RuntimeException("Listener0 can't have parameters");
    }

    public boolean isNotCalled() {
	return getCalledTimes() == 0;
    }

    public void onEvent() {
	timesCalled++;
    }

}
