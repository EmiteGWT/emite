package com.calclab.suco.testing.events;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MockedListener0Tests {

    @Test
    public void test() {
	final MockedListener0 listener = new MockedListener0();
	listener.onEvent();
	assertTrue(listener.isCalled());
	assertTrue(listener.isCalledOnce());
	assertTrue(listener.isCalled(1));
	listener.onEvent();
	assertTrue(listener.isCalled());
	assertFalse(listener.isCalledOnce());
	assertTrue(listener.isCalled(2));
    }
}
