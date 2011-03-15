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

import java.util.ArrayList;

import com.calclab.suco.client.events.Listener2;
import com.calclab.suco.testing.Logger;

/**
 * A test helper listener.
 * 
 * @see TestingSucoEventsExample
 */
public class MockedListener2<A, B> implements Listener2<A, B>, VerificableListener {
    private final ArrayList<A> paramsA;
    private final ArrayList<B> paramsB;

    public MockedListener2() {
	paramsA = new ArrayList<A>();
	paramsB = new ArrayList<B>();
    }

    public void clear() {
	paramsA.clear();
	paramsB.clear();
    }

    public int getCalledTimes() {
	return paramsA.size();
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
     * the number of arguments must be double of listener calls!
     */
    public boolean isCalledWithEquals(final Object... expectedOrderedValues) {
	if (paramsA.size() * 2 != expectedOrderedValues.length) {
	    Logger.debug("Expected called {0} times but was {1}", expectedOrderedValues.length / 2, paramsA.size());
	    return false;
	}
	final ArrayList<Object> expectedA = new ArrayList<Object>();
	final ArrayList<Object> expectedB = new ArrayList<Object>();
	for (int index = 0; index < expectedOrderedValues.length; index += 2) {
	    expectedA.add(expectedOrderedValues[index]);
	    expectedB.add(expectedOrderedValues[index + 1]);
	}

	for (int index = 0; index < paramsA.size(); index++) {
	    if (!expectedA.get(index).equals(paramsA.get(index))) {
		Logger.debug("Expected as first parameter equals to {0} but was {1}", expectedA.get(index), paramsA
			.get(index));
		return false;
	    }
	    if (!expectedB.get(index).equals(paramsB.get(index))) {
		Logger.debug("Expected as first parameter equals to {0} but was {1}", expectedB.get(index), paramsB
			.get(index));
		return false;
	    }
	}

	return true;
    }

    /**
     * the number of arguments must be double of listener calls!
     */
    public boolean isCalledWithSame(final Object... expectedOrderedValues) {
	if (paramsA.size() * 2 != expectedOrderedValues.length) {
	    Logger.debug("Expected called {0} times but was {1}", expectedOrderedValues.length / 2, paramsA.size());
	    return false;
	}
	final ArrayList<Object> expectedA = new ArrayList<Object>();
	final ArrayList<Object> expectedB = new ArrayList<Object>();
	for (int index = 0; index < expectedOrderedValues.length; index += 2) {
	    expectedA.add(expectedOrderedValues[index]);
	    expectedB.add(expectedOrderedValues[index + 1]);
	}

	for (int index = 0; index < paramsA.size(); index++) {
	    if (!(expectedA.get(index) == paramsA.get(index))) {
		Logger.debug("Expected as first parameter equals to {0} but was {1}", expectedA.get(index), paramsA
			.get(index));
		return false;
	    }
	    if (!(expectedB.get(index) == paramsB.get(index))) {
		Logger.debug("Expected as first parameter equals to {0} but was {1}", expectedB.get(index), paramsB
			.get(index));
		return false;
	    }
	}

	return true;
    }

    public boolean isNotCalled() {
	return getCalledTimes() == 0;
    }

    public void onEvent(final A paramA, final B paramB) {
	paramsA.add(paramA);
	paramsB.add(paramB);
    }
}
