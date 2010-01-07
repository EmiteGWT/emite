package com.calclab.emite.xfunctional.client.tests;

import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.xep.disco.client.DiscoveryManager;
import com.calclab.emite.xfunctional.client.Context;
import com.calclab.emite.xfunctional.client.FunctionalTestSuite;
import com.calclab.suco.client.Suco;

public class TestConnection implements FunctionalTestSuite {

    @Override
    public void afterLogin(Context ctx) {
	Session session = ctx.getSession();
	ctx.assertEquals("State should be disconnected", Session.State.disconnected, session.getState());
    }

    @Override
    public void beforeLogin(Context ctx) {
	DiscoveryManager discoveryManager = Suco.get(DiscoveryManager.class);
	discoveryManager.setActive(false);
	Session session = ctx.getSession();
	ctx.assertEquals("State should be disconnected", Session.State.disconnected, session.getState());
    }

    @Override
    public void duringLogin(Context ctx) {
	Session session = ctx.getSession();
	ctx.assertEquals("State should be connected", Session.State.ready, session.getState());
	session.logout();
    }

    @Override
    public String getName() {
	return "Connection test";
    }

}
