package com.calclab.emite.xep.storage.client;

import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.suco.client.ioc.Decorator;
import com.calclab.suco.client.ioc.Provider;
import com.calclab.suco.client.ioc.decorator.Singleton;
import com.calclab.suco.client.ioc.module.AbstractModule;
import com.calclab.suco.client.ioc.module.Factory;
import com.google.gwt.core.client.EntryPoint;

/**
 * Implements XEP http://xmpp.org/extensions/xep-0049.html
 */
public class PrivateStorageModule extends AbstractModule implements EntryPoint {

    @Override
    public void onModuleLoad() {
    }

    @Override
    public <T> Provider<T> register(final Class<? extends Decorator> decoratorType, final Class<T> componentType,
	    final Provider<T> provider) {
	// TODO Auto-generated method stub
	return null;
    }

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
