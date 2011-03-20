package com.calclab.suco.testing.ioc;

import java.lang.reflect.Constructor;
import java.util.HashMap;

import org.mockito.Mockito;

import com.calclab.suco.client.ioc.Container;
import com.calclab.suco.client.ioc.Decorator;
import com.calclab.suco.client.ioc.Provider;
import com.calclab.suco.client.ioc.decorator.NoDecoration;
import com.calclab.suco.client.ioc.decorator.Singleton;

/**
 * A Container that all its components are mocks (using mockito). You can create
 * components with the create method
 * 
 */
public class MockContainer implements Container {
    private final HashMap<Class<?>, Provider<?>> providers;

    public MockContainer() {
	providers = new HashMap<Class<?>, Provider<?>>();
    }

    /**
     * Create a instance of the given class if only one public constructor
     * available. Fill the constructor dependencies with the instances of the
     * container (mocks by default)
     * 
     * @param <T>
     * @param componentType
     *            the type of the component to be created
     * @return a instance of that component if only public constructor
     *         available.
     */
    public <T> T create(final Class<T> componentType) {
	return registerProvider(Singleton.instance, componentType, new Provider<T>() {
	    public T get() {
		return createInstance(componentType);
	    }
	}).get();
    }

    public <T> T getInstance(final Class<T> componentKey) {
	return getProvider(componentKey).get();
    }

    @SuppressWarnings("unchecked")
    public <T> Provider<T> getProvider(final Class<T> componentKey) {
	Provider<T> p = (Provider<T>) providers.get(componentKey);
	if (p == null) {
	    p = Singleton.instance.decorate(componentKey, createProvider(componentKey));
	    providers.put(componentKey, p);
	}
	return p;
    }

    public boolean hasProvider(final Class<?> componentKey) {
	return true;
    }

    public <T> Provider<T> registerInstance(final Class<T> type, final T instance) {
	return registerProvider(NoDecoration.instance, type, new Provider<T>() {
	    public T get() {
		return instance;
	    }
	});
    }

    /**
     * You can override the default singleton mocked providers
     */
    public <T> Provider<T> registerProvider(final Decorator decorator, final Class<T> componentType,
	    final Provider<T> provider) {
	final Provider<T> decorated = decorator.decorate(componentType, provider);
	providers.put(componentType, decorated);
	return decorated;
    }

    public <T> Provider<T> removeProvider(final Class<T> componentKey) {
	throw new RuntimeException("method not implemented");
    }

    /**
     * All the mocked providers have Singleton scope by default. You can
     * override this behaviour with this method
     * 
     * @param decorator
     *            The decorator to be applied
     * @param type
     *            the component type of the provider
     */
    public <T> Provider<T> setDecorationOfProvider(final Decorator decorator, final Class<T> type) {
	final Provider<T> p = decorator.decorate(type, createProvider(type));
	providers.put(type, p);
	return p;
    }

    @SuppressWarnings("unchecked")
    private <T> T createInstance(final Class<T> componentType) {
	final Constructor<T>[] constructors = (Constructor<T>[]) componentType.getConstructors();
	if (constructors.length != 1) {
	    throw new RuntimeException("Should be one and only one public constructor");
	}
	final Constructor<T> constructor = constructors[0];
	final Class<?>[] parameterTypes = constructor.getParameterTypes();
	final Object[] args = new Object[parameterTypes.length];
	for (int index = 0; index < parameterTypes.length; index++) {
	    args[index] = getInstance(parameterTypes[index]);
	}
	try {
	    return constructor.newInstance(args);
	} catch (final Exception e) {
	    throw new RuntimeException("problem creating the instance", e);
	}
    }

    private <T> Provider<T> createProvider(final Class<T> componentKey) {
	return new Provider<T>() {
	    public T get() {
		return Mockito.mock(componentKey);
	    }
	};
    }

}
