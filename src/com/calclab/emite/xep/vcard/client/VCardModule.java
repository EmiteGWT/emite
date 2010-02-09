package com.calclab.emite.xep.vcard.client;

import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.suco.client.ioc.decorator.Singleton;
import com.calclab.suco.client.ioc.module.AbstractModule;
import com.calclab.suco.client.ioc.module.Factory;

public class VCardModule extends AbstractModule {

	@Override
	protected void onInstall() {
		register(Singleton.class, new Factory<VCardManager>(VCardManager.class) {
			@Override
			public VCardManager create() {
				return new VCardManager($(Session.class));
			}
		});
	}
}
