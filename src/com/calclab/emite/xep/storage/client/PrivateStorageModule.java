package com.calclab.emite.xep.storage.client;

import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.suco.client.ioc.decorator.Singleton;
import com.calclab.suco.client.ioc.module.AbstractModule;
import com.calclab.suco.client.ioc.module.Factory;

/**
 * Implements XEP http://xmpp.org/extensions/xep-0049.html
 */
public class PrivateStorageModule extends AbstractModule {

    @Override
    protected void onInstall() {
	register(Singleton.class, new Factory<PrivateStorageManager>(PrivateStorageManager.class) {
	    @Override
	    public PrivateStorageManager create() {
		return new PrivateStorageManager($(Session.class));
	    }
	});
    }
}
