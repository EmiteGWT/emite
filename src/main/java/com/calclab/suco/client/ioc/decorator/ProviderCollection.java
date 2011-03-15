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

import java.util.ArrayList;
import java.util.List;

import com.calclab.suco.client.ioc.Container;
import com.calclab.suco.client.ioc.Decorator;
import com.calclab.suco.client.ioc.Provider;

/**
 * This decorator helps to make groups of providers for different purposes (i.e:
 * eager loading of a group of providers)
 * 
 * You should extend this class, usually in this way:
 * 
 * <pre>
 * public class SessionComponent extends ProviderCollection {
 *     public SessionComponent(final Container container) {
 * 	super(container, Singleton.instance);
 *     }
 * 
 *     public void createAll() {
 * 	for (final Provider&lt;?&gt; p : getProviders()) {
 * 	    p.get();
 * 	}
 *     }
 * }
 * </pre>
 * 
 * This class is a replacement of GroupedSingleton: it's not limited to
 * singletons and behave correctly when providers are removed from the
 * container.
 */
public abstract class ProviderCollection implements Decorator {
    private final Container container;
    private final Decorator delegate;
    private final ArrayList<Class<?>> types;

    public ProviderCollection(final Container container, final Decorator delegate) {
	this.container = container;
	this.delegate = delegate;
	types = new ArrayList<Class<?>>();
    }

    public <T> Provider<T> decorate(final Class<T> type, final Provider<T> undecorated) {
	// GWT.log("Adding to collection decorator" + type.getClass().getName(),
	// null);
	types.add(type);
	return delegate.decorate(type, undecorated);
    }

    /**
     * Return a list of all the providers of this collection. Take note that if
     * a provider is removed from the container, is not returned in this group
     * 
     * @return a list of all existent providers
     */
    public List<Provider<?>> getProviders() {
	final ArrayList<Provider<?>> providers = new ArrayList<Provider<?>>();
	for (final Class<?> type : types) {
	    if (container.hasProvider(type)) {
		providers.add(container.getProvider(type));
	    }
	}
	return providers;
    }
}
