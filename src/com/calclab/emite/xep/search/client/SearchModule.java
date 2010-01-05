package com.calclab.emite.xep.search.client;

import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.suco.client.Suco;
import com.calclab.suco.client.ioc.decorator.Singleton;
import com.calclab.suco.client.ioc.module.AbstractModule;
import com.calclab.suco.client.ioc.module.Factory;
import com.google.gwt.core.client.EntryPoint;

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
public class SearchModule extends AbstractModule implements EntryPoint {

    public SearchModule() {
        super();
    }

    @Override
    public void onModuleLoad() {
        Suco.install(this);
    }

    @Override
    protected void onInstall() {
        register(Singleton.class, new Factory<SearchManagerImpl>(SearchManagerImpl.class) {
            @Override
            public SearchManagerImpl create() {
                return new SearchManagerImpl($(Session.class));
            }
        });
    }

}
