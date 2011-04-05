package com.calclab.emite.reconnect.client;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;

/**
 * When installing this module, emite will try to reconnect again with same
 * credentials when connection is closed
 */
public class ReconnectModule extends AbstractGinModule {

    @Override
    protected void configure() {
	bind(SessionReconnect.class).in(Singleton.class);
    }

}
