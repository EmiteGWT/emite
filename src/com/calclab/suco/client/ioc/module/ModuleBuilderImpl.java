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
import com.calclab.suco.client.ioc.decorator.NoDecoration;

/**
 * @see ModuleBuilder documentation
 * 
 */
public class ModuleBuilderImpl implements ModuleBuilder {
    protected Container container;

    public void install(final SucoModule... modules) {
	for (final SucoModule sucoModule : modules) {
	    sucoModule.onInstall(container);
	}
    }

    public <T> Provider<T> register(final Class<? extends Decorator> decoratorType, final Class<T> componentType,
	    final Provider<T> provider) {
	return container.registerProvider(container.getInstance(decoratorType), componentType, provider);
    }

    public void register(final Class<? extends Decorator> decoratorType, final Factory<?>... factories) {
	register(container.getInstance(decoratorType), factories);
    }

    public <T> Provider<T> register(final Class<T> componentType, final Provider<T> provider) {
	return container.registerProvider(null, componentType, provider);
    }

    public <T> Provider<T> register(final Decorator decorator, final Class<T> componentType, final Provider<T> provider) {
	assert decorator != null;
	return container.registerProvider(decorator, componentType, provider);
    }

    public void register(final Decorator decorator, final Factory<?>... factories) {
	assert decorator != null;
	for (final Factory<?> factory : factories) {
	    registerFactory(decorator, factory);
	}
    }

    public void register(final Factory<?>... factories) {
	register(NoDecoration.instance, factories);
    }

    public <D extends Decorator> void registerDecorator(final Class<D> type, final D decorator) {
	container.registerProvider(null, type, new Provider<D>() {
	    public D get() {
		return decorator;
	    }
	});
    };

    public void setContainer(final Container container) {
	this.container = container;
    }

    <O> void registerFactory(final Decorator decorator, final Factory<O> factory) {
	container.registerProvider(decorator, factory.getType(), factory);
    }
}
