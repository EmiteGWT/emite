package com.calclab.emite.reconnect.client;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules(ReconnectModule.class)
public interface ReconnectGinjector extends Ginjector {
    SessionReconnect getSessionReconnect();
}
