package com.calclab.emite.xfunctional.client.tests;

import java.util.List;

import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.xep.disco.client.DiscoveryManager;
import com.calclab.emite.xep.search.client.SearchManager;
import com.calclab.emite.xfunctional.client.Context;
import com.calclab.emite.xfunctional.client.FunctionalTest;
import com.calclab.suco.client.Suco;
import com.calclab.suco.client.events.Listener;

public class TestSearchRetrieveFields implements FunctionalTest {

    @Override
    public void afterLogin(Context ctx) {
    }

    @Override
    public void beforeLogin(Context ctx) {
	ctx.info("Disable discovery");
	DiscoveryManager discoveryManager = Suco.get(DiscoveryManager.class);
	discoveryManager.setActive(false);

    }

    @Override
    public void duringLogin(final Context ctx) {
	ctx.info("Requesting fields...");
	final Session session = ctx.getSession();
	SearchManager search = Suco.get(SearchManager.class);
	search.setHost(XmppURI.uri("vjud.localhost"));
	search.requestSearchFields(new Listener<List<String>>() {
	    @Override
	    public void onEvent(List<String> parameter) {
		ctx.success("Search fields retrieved");
		session.logout();
	    }
	});

    }

    @Override
    public String getName() {
	return "Retrieve Search Fields test";
    }

}
