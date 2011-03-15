package com.calclab.emite.xep.search.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;

/**
 * Implements XEP-0055: Jabber Search
 * 
 * This specification provides canonical documentation of the jabber:iq:search
 * namespace currently in use within the Jabber community.
 * 
 * @see http://www.xmpp.org/extensions/xep-0055.html
 * 
 * 
 */
public class SearchModule extends AbstractGinModule implements EntryPoint {

    @Override
    public void onModuleLoad() {
    }

    @Override
    protected void configure() {
	bind(SearchManager.class).to(SearchManagerImpl.class).in(Singleton.class);
    }

}
