package com.calclab.suco.testing.events;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MockedListenerTests {

    @Test
    public void simple() {
	final MockedListener<String> listener = new MockedListener<String>();
	assertFalse(listener.isCalled());
	listener.onEvent("One");
	listener.onEvent("Two");
	assertTrue(listener.isCalled());
	assertTrue(listener.isCalledWithEquals("One", "Two"));
	assertFalse(listener.isCalledWithEquals("Two", "One"));
	assertFalse(listener.isCalledWithSame("One-", "Two"));
    }
}
