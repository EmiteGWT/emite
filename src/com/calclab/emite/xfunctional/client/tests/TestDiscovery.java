package com.calclab.emite.xfunctional.client.tests;

import com.calclab.emite.xep.disco.client.DiscoveryManager;
import com.calclab.emite.xfunctional.client.Context;
import com.calclab.emite.xfunctional.client.FunctionalTest;
import com.calclab.suco.client.Suco;
import com.calclab.suco.client.events.Listener;

public class TestDiscovery implements FunctionalTest {

    @Override
    public void afterLogin(Context ctx) {
    }

    @Override
    public void beforeLogin(Context ctx) {
	DiscoveryManager discoveryManager = Suco.get(DiscoveryManager.class);
	discoveryManager.setActive(true);
    }

    @Override
    public void duringLogin(final Context ctx) {
	ctx.info("Discovery test started!");
	DiscoveryManager discovery = Suco.get(DiscoveryManager.class);

	discovery.onReady(new Listener<DiscoveryManager>() {
	    @Override
	    public void onEvent(DiscoveryManager manager) {
		ctx.success("Discovery features received");
		ctx.getSession().logout();
	    }
	});
    }

    @Override
    public String getName() {
	return "Discovery test";
    }
}
