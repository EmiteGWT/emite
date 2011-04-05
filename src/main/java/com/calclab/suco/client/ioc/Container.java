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

/**
 * A object IoC container. In fact is a Provider container. When a instance is
 * required from the container, it ask the provider to get it.
 */
public interface Container {
    /**
     * Check if a provider of associated to the given class literal is
     * registered in the container
     * 
     * @param componentKey
     *            the component key literal
     * @return true if the provider is present
     */
    public boolean hasProvider(Class<?> componentKey);

    /**
     * Removes the provider associated to the component key if present. Does
     * nothing if not exists.
     * 
     * @param <T>
     * @param componentKey
     * @return
     */
    public <T> Provider<T> removeProvider(Class<T> componentKey);

    /**
     * Obtain a component registered with the given key from the container
     * 
     * @param <T>
     * @param componentType
     * @return The component, throw RuntimeException if the given component
     *         doesn't exists
     */
    <T> T getInstance(Class<T> componentKey);

    /**
     * Obtain a provider of the component type from the container
     * 
     * @param <T>
     * @param componentType
     *            The type literal of the desired component
     * @return The provider, throw RuntimeException if the given provider
     *         doesn't exists
     */
    <T> Provider<T> getProvider(Class<T> componentKey);

    /**
     * Modifies the provider by the decorator (if given), and register it in
     * container associated to the given type literal.
     * 
     * @param <T>
     *            The type of the object provided
     * @param decorator
     *            The decorator to be applied to the provider. Can be null to
     *            skip the decoration stage.
     * @param componentType
     *            The type literal of the component provided
     * @param provider
     *            The provider to be modified and then registered
     * @return The provider after the decoration
     */
    <T> Provider<T> registerProvider(Decorator decorator, Class<T> componentType, Provider<T> provider);

}
