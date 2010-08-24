package com.calclab.suco.testing.events;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MockedListener2Tests {

    @Test
    public void test() {
	final MockedListener2<String, String> listener = new MockedListener2<String, String>();
	listener.onEvent("one", "two");
	listener.onEvent("three", "four");
	assertTrue(listener.isCalled(2));
	assertTrue(listener.isCalledWithEquals("one", "two", "three", "four"));
	assertTrue(listener.isCalledWithSame("one", "two", "three", "four"));
    }
}
