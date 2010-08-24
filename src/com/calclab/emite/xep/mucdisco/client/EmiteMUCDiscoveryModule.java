package com.calclab.emite.xep.mucdisco.client;

import com.calclab.emite.xep.disco.client.DiscoveryManager;
import com.calclab.suco.client.Suco;
import com.calclab.suco.client.ioc.decorator.Singleton;
import com.calclab.suco.client.ioc.module.AbstractModule;
import com.calclab.suco.client.ioc.module.Factory;
import com.google.gwt.core.client.EntryPoint;

public class EmiteMUCDiscoveryModule extends AbstractModule implements EntryPoint {

    @Override
    public void onInstall() {
	register(Singleton.class, new Factory<RoomDiscoveryManager>(RoomDiscoveryManager.class) {
	    @Override
	    public RoomDiscoveryManager create() {
		return new RoomDiscoveryManagerImpl($(DiscoveryManager.class));
	    }
	});
    }

    @Override
    public void onModuleLoad() {
	Suco.install(this);
    }
}
