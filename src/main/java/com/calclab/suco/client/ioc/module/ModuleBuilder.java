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
import com.calclab.suco.client.ioc.Decorator;
import com.calclab.suco.client.ioc.Provider;

/**
 * Help to perform the component registrarion. It allows the use of the Factory
 * abstraction to reduce the boilerplate and do OAP style programming
 */
public interface ModuleBuilder {

    /**
     * Install the given modules in the container
     * 
     * @param modules
     *            a list of modules to be installed
     */
    public void install(final SucoModule... modules);

    /**
     * Register the provider after being decorated. The decorator of the given
     * type should be installed previously on the container
     * 
     * @param decoratorType
     *            the decorator type literal
     * @param componentType
     *            the component type literal
     * @param provider
     *            the given provider
     * @returns the decorated provider
     */
    public <T> Provider<T> register(final Class<? extends Decorator> decoratorType, final Class<T> componentType,
	    final Provider<T> provider);

    /**
     * Register all the given FactoryProviders after being decorated by a
     * decorator of the given decorator type. The decorator should be previously
     * registered
     * 
     * Example: register(SingletonScope.class, ...);
     * 
     * @param decoratorType
     *            the decorator to be applied to all the providers
     * @param providerInfos
     *            a list of factories to be registered in the container
     */
    public void register(final Class<? extends Decorator> decoratorType, final Factory<?>... factories);

    /**
     * Register the provider with no decoration
     * 
     * @param <T>
     *            the type provided
     * @param componentType
     *            the type literal of the component
     * @param provider
     *            the provider
     * @return the provider
     */
    public <T> Provider<T> register(final Class<T> componentType, final Provider<T> provider);

    /**
     * Register the provider after being decorated. Decorator can NOT be null
     * 
     * @param <T>
     *            The type of the object provided
     * @param decorator
     *            The decorator. Can NOT be null
     * @param componentType
     *            The component type literal
     * @param provider
     *            The provider
     * @return The decorated provider
     */
    public <T> Provider<T> register(Decorator decorator, final Class<T> componentType, final Provider<T> provider);

    /**
     * Register all the given factories after being decorated by the given
     * decorator. The decorator can NOT be null
     * 
     * Example: register(SingletonScope.class, ...);
     * 
     * @param decorator
     *            the decorator to be applied. Can NOT be null
     * @param factories
     *            a list of factories to be registered in the container
     */
    public void register(Decorator decorator, final Factory<?>... factories);

    /**
     * Register all the given factories with no decorator
     * 
     * @param factories
     *            the factories to be registered
     */
    public void register(Factory<?>... factories);

    /**
     * Register a new decorator
     * 
     * @param <D>
     *            the decorator type
     * @param type
     *            the decorator type literal
     * @param decorator
     *            the decorator to be registered
     */
    public <D extends Decorator> void registerDecorator(final Class<D> type, final D decorator);

    public void setContainer(Container container);

}
