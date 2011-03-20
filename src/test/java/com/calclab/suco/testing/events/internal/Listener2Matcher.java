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
/**
 *
 */
package com.calclab.suco.testing.events.internal;

import com.calclab.suco.client.events.Listener2;
import com.calclab.suco.testing.events.Eventito.ParamHolder;

public class Listener2Matcher<A, B> extends BaseListenerMatcher<Listener2<A, B>> {
    public Listener2Matcher(final ParamHolder param) {
	super(param);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void perform(final Object listener, final ParamHolder param) {
	((Listener2) listener).onEvent(param.getParam1(), param.getParam2());
    }

}
