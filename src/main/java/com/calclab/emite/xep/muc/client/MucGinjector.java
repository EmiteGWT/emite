package com.calclab.emite.xep.muc.client;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules(MucModule.class)
public interface MucGinjector extends Ginjector {
    RoomManager getRoomManager();
}
