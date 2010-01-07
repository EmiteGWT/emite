package com.calclab.emite.xfunctional.client.tests;

import java.util.HashMap;
import java.util.List;

import com.calclab.emite.browser.client.PageAssist;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.xep.disco.client.DiscoveryManager;
import com.calclab.emite.xep.search.client.Item;
import com.calclab.emite.xep.search.client.SearchManager;
import com.calclab.emite.xep.search.client.SearchResult;
import com.calclab.emite.xep.search.client.SearchResult.Status;
import com.calclab.emite.xfunctional.client.Context;
import com.calclab.emite.xfunctional.client.FunctionalTest;
import com.calclab.suco.client.Suco;
import com.calclab.suco.client.events.Listener;

public class SearchTests extends BasicTestSuite {

    FunctionalTest requestFields = new FunctionalTest() {
	@Override
	public void run(final Context ctx) {
	    ctx.info("Requesting fields...");

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
    };

    FunctionalTest performSearch = new FunctionalTest() {
	@Override
	public void run(final Context ctx) {
	    ctx.info("Performing search...");

	    HashMap<String, String> query = new HashMap<String, String>();
	    query.put("nick", "test*");
	    search.search(query, new Listener<SearchResult<List<Item>>>() {
		@Override
		public void onEvent(SearchResult<List<Item>> result) {
		    ctx.success("Search result retrieved");

		    if (result.status == Status.success) {
			for (Item item : result.data) {
			    ctx.info("Result: " + item.getNick());
			}
		    }
		    session.logout();
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
