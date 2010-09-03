package com.calclab.emite.reconnect.client;

import com.google.gwt.inject.client.GinModules;

@GinModules(ReconnectModule.class)
public interface ReconnectGinjector {
    public SessionReconnect getSessionReconnect();
}
