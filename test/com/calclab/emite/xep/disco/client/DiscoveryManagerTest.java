package com.calclab.emite.xep.disco.client;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.testing.MockedSession;
import com.calclab.suco.testing.events.MockedListener;

public class DiscoveryManagerTest {

    private DiscoveryManager manager;
    private MockedSession session;

    @Before
    public void beforeTests() {
	session = new MockedSession();
	manager = new DiscoveryManager(session);
    }

    @Test
    public void shouldInformListeners() {
	final MockedListener<DiscoveryManager> listener = new MockedListener<DiscoveryManager>();
	manager.onReady(listener);

    }
}
