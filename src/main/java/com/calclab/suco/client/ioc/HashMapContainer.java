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

package com.calclab.suco.client.ioc;

import java.util.HashMap;

/**
 * A basic implementation of container
 */
@SuppressWarnings("serial")
public class HashMapContainer extends HashMap<Class<?>, Provider<?>> implements Container {

    public HashMapContainer() {
    }

    public <T> T getInstance(final Class<T> componentKey) {
	return getProvider(componentKey).get();
    }

    @SuppressWarnings("unchecked")
    public <T> Provider<T> getProvider(final Class<T> componentKey) {
	final Provider<T> provider = (Provider<T>) get(componentKey);
	if (provider == null) {
	    // TODO: keySet to string
	    final String message = "getProvider failed: component of type " + componentKey
		    + " not registered. Registered component keys: [" + keySet() + "]";
	    throw new RuntimeException(message);
	}
	return provider;
    }

    public boolean hasProvider(final Class<?> componentKey) {
	return containsKey(componentKey);
    }

    public <T> Provider<T> registerProvider(final Decorator decorator, final Class<T> componentType,
	    final Provider<T> provider) {
	if (containsKey(componentType)) {
	    throw new RuntimeException("Provider of type " + componentType + " already registered.");
	}
	// GWT.log("Registering provider of type: " + componentType, null);
	final Provider<T> decoratedProvider = decorator == null ? provider : decorator
		.decorate(componentType, provider);
	put(componentType, decoratedProvider);
	return decoratedProvider;
    }

    @SuppressWarnings("unchecked")
    public <T> Provider<T> removeProvider(final Class<T> componentKey) {
	return (Provider<T>) remove(componentKey);
    }
}
