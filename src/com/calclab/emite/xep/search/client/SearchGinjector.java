package com.calclab.emite.xep.search.client;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules(SearchModule.class)
public interface SearchGinjector extends Ginjector {
    SearchManager getSearchManager();
}
