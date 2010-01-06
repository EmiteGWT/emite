package com.calclab.emite.xfunctional.client.tests;

import java.util.List;

import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.xep.search.client.SearchManager;
import com.calclab.emite.xfunctional.client.Context;
import com.calclab.emite.xfunctional.client.FunctionalTest;
import com.calclab.suco.client.Suco;
import com.calclab.suco.client.events.Listener;

public class TestSearchRetrieveSearchFields implements FunctionalTest {

    @Override
    public void afterLogin(Context ctx) {
    }

    @Override
    public void beforeLogin(Context ctx) {
    }

    @Override
    public void duringLogin(final Context ctx) {
	final Session session = ctx.getSession();
	SearchManager search = Suco.get(SearchManager.class);
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
