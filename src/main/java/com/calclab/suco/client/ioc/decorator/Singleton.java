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
package com.calclab.suco.client.ioc.decorator;

import com.calclab.suco.client.ioc.Decorator;
import com.calclab.suco.client.ioc.Provider;

/**
 * A singleton scope creates a provider that always returns same instance
 * 
 */
public class Singleton implements Decorator {
    public final static Singleton instance = new Singleton();

    /**
     * Use Scopes class
     */
    private Singleton() {
    }

    public <T> Provider<T> decorate(final Class<T> type, final Provider<T> unscoped) {
	return new Provider<T>() {
	    private T instance;

	    public T get() {
		if (instance == null) {
		    // GWT.log("Creating singleton instance of type " + type,
		    // null);
		    instance = unscoped.get();
		}
		return instance;
	    }
	};
    }
}
