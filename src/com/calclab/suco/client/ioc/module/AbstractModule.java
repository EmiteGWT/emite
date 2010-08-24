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

import com.calclab.suco.client.ioc.Container;
import com.calclab.suco.client.ioc.Provider;

/**
 * The default class to extend to create a Module. Implements all the facilities
 * of the ModuleBuilder with a couple of helper methods
 */
public abstract class AbstractModule extends ModuleBuilderImpl implements SucoModule {

    public AbstractModule() {
    }

    public void onInstall(final Container container) {
	this.container = container;
	onInstall();
    }

    /**
     * Get a instance of the specified component key
     * 
     * @param <T>
     *            The component key
     * @param componentType
     *            The component key
     * @return The component instance
     */
    protected <T> T $(final Class<T> componentType) {
	return container.getInstance(componentType);
    }

    /**
     * Get a provider of the specified component key
     * 
     * @param <T>
     *            The component key
     * @param componentType
     *            The component key
     * @return The provider of that component key
     */
    protected <T> Provider<T> $$(final Class<T> componentType) {
	return container.getProvider(componentType);
    }

    protected abstract void onInstall();

}
