package com.calclab.emite.xep.mucdisco.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;

public class MucDiscoveryModule extends AbstractGinModule implements EntryPoint {

    @Override
    public void onModuleLoad() {
    }

    @Override
    protected void configure() {
	bind(RoomDiscoveryManager.class).to(RoomDiscoveryManagerImpl.class).in(Singleton.class);
    }

}
