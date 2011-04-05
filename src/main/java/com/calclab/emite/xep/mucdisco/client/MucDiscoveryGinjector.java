package com.calclab.emite.xep.mucdisco.client;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules(MucDiscoveryModule.class)
public interface MucDiscoveryGinjector extends Ginjector {
    RoomDiscoveryManager getRoomDiscoveryManager();
}
