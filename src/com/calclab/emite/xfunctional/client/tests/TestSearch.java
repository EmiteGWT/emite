package com.calclab.emite.xfunctional.client.tests;

import java.util.List;

import com.calclab.emite.browser.client.PageAssist;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.xep.disco.client.DiscoveryManager;
import com.calclab.emite.xep.search.client.SearchManager;
import com.calclab.emite.xep.search.client.SearchResult;
import com.calclab.emite.xep.search.client.SearchResult.Status;
import com.calclab.emite.xfunctional.client.Context;
import com.calclab.emite.xfunctional.client.FunctionalTestSuite;
import com.calclab.suco.client.Suco;
import com.calclab.suco.client.events.Listener;

public class TestSearch implements FunctionalTestSuite {

    @Override
    public void afterLogin(Context ctx) {
    }

    @Override
    public void beforeLogin(Context ctx) {
	ctx.info("Disable discovery");
	DiscoveryManager discoveryManager = Suco.get(DiscoveryManager.class);
	discoveryManager.setActive(false);

	SearchManager search = Suco.get(SearchManager.class);
	String searchHost = PageAssist.getMeta("emite.searchHost");
	ctx.info("Using " + searchHost + " as search host. Configured at emite.searchHost meta parameter in html page");
	search.setHost(XmppURI.uri(searchHost));
    }

    @Override
    public void duringLogin(final Context ctx) {

    }

    @Override
    public String getName() {
	return "Search Tests";
    }

    private void requestFields(final Context ctx) {
	ctx.info("Requesting fields...");
	final Session session = ctx.getSession();
	SearchManager search = Suco.get(SearchManager.class);

	search.requestSearchFields(new Listener<SearchResult<List<String>>>() {
	    @Override
	    public void onEvent(SearchResult<List<String>> result) {
		ctx.success("Search fields retrieved");

		if (result.status == Status.success) {
		    for (String name : result.data) {
			ctx.info("Field retrieved: " + name);
		    }
		} else {
		    ctx.info("No fields retrieved");
		}

		session.logout();
	    }
	});
    }

}
