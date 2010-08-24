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
package com.calclab.suco.client;

import com.calclab.suco.client.ioc.Container;
import com.calclab.suco.client.ioc.module.SucoModule;

/**
 * A singleton container, enough for most applications. In the (rare) case you
 * need multiple containers, you can use SucoFactory.
 */
public class Suco {

    private static Container components = SucoFactory.create();

    /**
     * Obtain a instance from the Suco container
     * 
     * @param <T>
     * @param componentType
     * @return
     */
    public static <T> T get(final Class<T> componentType) {
	return components.getInstance(componentType);
    }

    /**
     * Get the Suco container itself.
     * 
     * @return the Suco singleton container
     */
    public static Container getComponents() {
	return components;
    }

    /**
     * Install the given modules into the container
     * 
     * @param modules
     *            the list of modules to be installed in the singleton Suco
     *            container
     */
    public static void install(final SucoModule... modules) {
	for (final SucoModule sucoModule : modules) {
	    sucoModule.onInstall(components);
	}
    }

    /**
     * Change the Suco container. It's a kind of switch context
     * 
     * @param newContainer
     *            the new container (can't be null)
     * @return the old container
     */
    public static Container switchContainer(Container newContainer) {
	assert newContainer != null : "Container not specified in switchContainer";
	Container old = components;
	components = newContainer;
	return old;
    }
}
