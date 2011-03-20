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

import org.mockito.Mockito;

import com.calclab.suco.client.events.Listener;
import com.calclab.suco.client.events.Listener0;
import com.calclab.suco.client.events.Listener2;
import com.calclab.suco.testing.events.internal.Listener0Matcher;
import com.calclab.suco.testing.events.internal.Listener2Matcher;
import com.calclab.suco.testing.events.internal.ListenerMatcher;

/**
 * A fluid-style class to help testing events
 * 
 * @see TestingSucoEventsExample
 * 
 */
@SuppressWarnings("unchecked")
public class Eventito {
    public static class ParamHolder {
	private final Object param1;
	private final Object param2;

	public ParamHolder(final Object param1, final Object param2) {
	    this.param1 = param1;
	    this.param2 = param2;
	}

	public Object getParam1() {
	    return param1;
	}

	public Object getParam2() {
	    return param2;
	}

	public <T> T when(final T publisher) {
	    return Mockito.verify(publisher);
	}
    }

    private static ParamHolder currentParam;

    public static Listener anyListener() {
	return (Listener) Mockito.argThat(new ListenerMatcher(currentParam));
    }

    public static <T> Listener<T> anyListener(final Class<T> listenerParamType) {
	return Mockito.argThat(new ListenerMatcher<T>(currentParam));
    }

    public static Listener0 anyListener0() {
	return Mockito.argThat(new Listener0Matcher(currentParam));
    }

    public static <A, B> Listener2<A, B> anyListener2(final Class<A> listenerParam1Type,
	    final Class<B> listenerParam2Type) {
	return Mockito.argThat(new Listener2Matcher<A, B>(currentParam));
    }

    public static ParamHolder fire() {
	currentParam = new ParamHolder(null, null);
	return currentParam;
    }

    public static ParamHolder fire(final Object param) {
	currentParam = new ParamHolder(param, null);
	return currentParam;
    }

    public static ParamHolder fire(final Object param1, final Object param2) {
	currentParam = new ParamHolder(param1, param2);
	return currentParam;
    }
}
