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

import com.calclab.suco.client.events.Listener;
import com.calclab.suco.testing.Logger;

public class MockedListener<A> implements Listener<A>, VerificableListener {
    private final ArrayList<A> parameters;

    public MockedListener() {
	parameters = new ArrayList<A>();
    }

    public void clear() {
	parameters.clear();
    }

    public int getCalledTimes() {
	return parameters.size();
    }

    public A getValue(final int index) {
	return parameters.get(index);
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
     * The number of the values must be the same of number of calls this
     * listener received
     */
    public boolean isCalledWithEquals(final Object... expectedOrderedValues) {
	if (parameters.size() != expectedOrderedValues.length) {
	    Logger.debug("Expected called {0} times but was {1}", expectedOrderedValues.length, parameters.size());
	    return false;
	}
	for (int index = 0; index < expectedOrderedValues.length; index++) {
	    final Object expected = expectedOrderedValues[index];
	    final A actual = parameters.get(index);
	    if (!expected.equals(actual)) {
		Logger.debug("Expected equals to {0} but was {1}", expected, actual);
		return false;
	    }
	}
	return true;
    }

    /**
     * The number of the values must be the same of number of calls this
     * listener received
     */
    public boolean isCalledWithSame(final Object... expectedOrderedValues) {
	if (parameters.size() != expectedOrderedValues.length) {
	    Logger.debug("Expected called {0} times but was {1}", expectedOrderedValues.length, parameters.size());
	    return false;
	}
	for (int index = 0; index < expectedOrderedValues.length; index++) {
	    final Object expected = expectedOrderedValues[index];
	    final A actual = parameters.get(index);
	    if (!(expected == actual)) {
		Logger.debug("Expected same {0} but was {1}", expected, actual);
		return false;
	    }
	}
	return true;
    }

    public boolean isNotCalled() {
	return getCalledTimes() == 0;
    }

    public void onEvent(final A parameter) {
	parameters.add(parameter);
    }

}
