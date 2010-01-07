package com.calclab.emite.xfunctional.client.tests;

import java.util.HashMap;
import java.util.List;

import com.calclab.emite.browser.client.PageAssist;
import com.calclab.emite.core.client.xmpp.session.ResultListener;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.xep.disco.client.DiscoveryManager;
import com.calclab.emite.xep.search.client.Item;
import com.calclab.emite.xep.search.client.SearchFields;
import com.calclab.emite.xep.search.client.SearchManager;
import com.calclab.emite.xfunctional.client.Context;
import com.calclab.emite.xfunctional.client.FunctionalTest;
import com.calclab.suco.client.Suco;

public class SearchTests extends BasicTestSuite {

    FunctionalTest requestFields = new FunctionalTest() {
	@Override
	public void run(final Context ctx) {
	    ctx.info("Requesting fields...");

	    search.requestSearchFields(new ResultListener<SearchFields>() {
		@Override
		public void onFailure(String message) {
		    ctx.success("Search fields retrieved");
		    ctx.info("No fields retrieved");
		    session.logout();
		}

		@Override
		public void onSuccess(SearchFields fields) {
		    ctx.success("Search fields retrieved");
		    for (String name : fields.getFieldNames()) {
			ctx.info("Field retrieved: " + name);
		    }
		    session.logout();
		}
	    });
	}

    };

    FunctionalTest performSearch = new FunctionalTest() {
	@Override
	public void run(final Context ctx) {
	    ctx.info("Performing search...");

	    HashMap<String, String> query = new HashMap<String, String>();
	    query.put("nick", "test*");

	    search.search(query, new ResultListener<List<Item>>() {

		@Override
		public void onFailure(String message) {
		    ctx.success("Search result retrieved");
		    session.logout();

		}

		@Override
		public void onSuccess(List<Item> items) {
		    ctx.success("Search result retrieved");
		    session.logout();
		    for (Item item : items) {
			ctx.info("Result: " + item.getNick());
		    }

		}
	    });

	}
    };

    private final Session session;
    private final SearchManager search;

    public SearchTests() {
	session = Suco.get(Session.class);
	search = Suco.get(SearchManager.class);
    }

    @Override
    public void beforeLogin(Context ctx) {
	DiscoveryManager discoveryManager = Suco.get(DiscoveryManager.class);
	discoveryManager.setActive(false);

	String searchHost = PageAssist.getMeta("emite.searchHost");
	ctx.info("Using " + searchHost + " as search host. Configured at emite.searchHost meta parameter in html page");
	search.setHost(XmppURI.uri(searchHost));
    }

    @Override
    public void registerTests() {
	add("Request search fields", requestFields);
	add("Perform search", performSearch);
    }

}
