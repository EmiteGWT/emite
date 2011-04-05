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
package com.calclab.suco.client.ioc.module;

import com.calclab.suco.client.ioc.Provider;

/**
 * A Factory is a class that is able to create object instances. You should
 * override create() method and, optionally the onAfterCreated method
 * 
 * @param <T>
 *            The object instance type
 */
public abstract class Factory<T> implements Provider<T> {
    private final Class<T> type;
    private T currentInstance;

    public Factory(final Class<T> type) {
	this.currentInstance = null;
	this.type = type;
    }

    public abstract T create();

    public final T get() {
	if (currentInstance == null) {
	    currentInstance = create();
	    final T instance = currentInstance;
	    onAfterCreated(currentInstance);
	    currentInstance = null;
	    return instance;
	} else {
	    return currentInstance;
	}
    }

    public final Class<T> getType() {
	return type;
    }

    public void onAfterCreated(final T instance) {
    }

}
