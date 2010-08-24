package com.calclab.suco.testing.ioc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.calclab.suco.client.ioc.Provider;
import com.calclab.suco.client.ioc.decorator.NoDecoration;
import com.calclab.suco.client.ioc.decorator.Singleton;

public class MockContainerTests {

    public static class Component {
	public final Object object;
	public final Exception exception;

	public Component(final Object object, final Exception exception) {
	    this.object = object;
	    this.exception = exception;
	}
    }

    public static class StringDependant {
	public final String message;

	public StringDependant(final String message) {
	    this.message = message;
	}
    }

    @Test
    public void allMockedInstancesAreSingletonByDefault() {
	final MockContainer c = new MockContainer();
	assertSame(c.getInstance(Object.class), c.getInstance(Object.class));
    }

    @Test
    public void shouldAlwaysHaveAProvider() {
	final MockContainer c = new MockContainer();
	assertTrue(c.hasProvider(Object.class));
	assertNotNull(c.getProvider(Object.class));
    }

    @Test
    public void shouldAlwaysReturnInstances() {
	final MockContainer c = new MockContainer();
	assertNotNull(c.getInstance(Exception.class));
	assertNotNull(c.getInstance(Object.class));
    }

    @Test
    public void shouldBeAbleToChangeScopeOfMockedProviders() {
	final MockContainer c = new MockContainer();
	c.setDecorationOfProvider(NoDecoration.instance, Object.class);
	assertNotSame(c.getInstance(Object.class), c.getInstance(Object.class));
    }

    @Test
    public void shouldBeAbleToOverrideProviders() {
	final MockContainer c = new MockContainer();
	c.registerProvider(Singleton.instance, String.class, new Provider<String>() {
	    public String get() {
		return "Hola!";
	    }
	});
	final StringDependant instance = c.create(StringDependant.class);
	assertEquals("Hola!", instance.message);
    }

    @Test
    public void shouldBeAbleToRegisterInstances() {
	final MockContainer c = new MockContainer();
	c.registerInstance(String.class, "Adiós!");
	final StringDependant instance = c.create(StringDependant.class);
	assertEquals("Adiós!", instance.message);
    }

    @Test
    public void shouldCreateInstancesWithOneContstructor() {
	final MockContainer c = new MockContainer();
	final Component instance = c.create(Component.class);
	assertNotNull(instance);
	assertNotNull(instance.object);
	assertSame(instance.object, c.getInstance(Object.class));
	assertNotNull(instance.exception);
	assertSame(instance.exception, c.getInstance(Exception.class));
    }

    @Test
    public void shouldReturnInstanceProvider() {
	final MockContainer c = new MockContainer();
	final Component instance = c.create(Component.class);
	final Provider<Component> provider = c.getProvider(Component.class);
	assertNotNull(provider);
	assertSame(instance, provider.get());
    }
}
