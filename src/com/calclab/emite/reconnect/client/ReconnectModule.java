package com.calclab.emite.reconnect.client;

import com.calclab.emite.core.client.bosh.Connection;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.session.SessionComponent;
import com.calclab.suco.client.Suco;
import com.calclab.suco.client.ioc.module.AbstractModule;
import com.calclab.suco.client.ioc.module.Factory;
import com.google.gwt.core.client.EntryPoint;

/**
 * When installing this module, emite will try to reconnect again with same
 * credentials when connection is closed
 */
public class ReconnectModule extends AbstractModule implements EntryPoint {

    @Override
    public void onModuleLoad() {
	Suco.install(this);
    }

    @Override
    protected void onInstall() {
	register(SessionComponent.class, new Factory<SessionReconnect>(SessionReconnect.class) {
	    @Override
	    public SessionReconnect create() {
		return new SessionReconnect($(Connection.class), $(Session.class));
	    }
	});
    }

}
