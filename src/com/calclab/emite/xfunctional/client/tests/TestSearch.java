package com.calclab.emite.xfunctional.client.tests;

import java.util.List;

import com.calclab.emite.xep.search.client.SearchManager;
import com.calclab.emite.xfunctional.client.FunctionalTest;
import com.calclab.suco.client.Suco;
import com.calclab.suco.client.events.Listener;

public class TestSearch extends FunctionalTest {

    @Override
    public String getName() {
	return "Search test";
    }

    @Override
    public void run() {
	SearchManager search = Suco.get(SearchManager.class);
	testBegins();
	search.requestSearchFields(new Listener<List<String>>() {
	    @Override
	    public void onEvent(List<String> parameter) {
		testEnds();
	    }
	});
    }

}
