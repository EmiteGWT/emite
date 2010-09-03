package com.calclab.emite.xep.disco.client;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules(DiscoveryModule.class)
public interface DiscoveryGinjector extends Ginjector {
    DiscoveryManager getDiscoveryManager();
}
