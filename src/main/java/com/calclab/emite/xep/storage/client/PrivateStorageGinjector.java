package com.calclab.emite.xep.storage.client;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules(PrivateStorageModule.class)
public interface PrivateStorageGinjector extends Ginjector {
    PrivateStorageManager getPrivateStorageManager();
}
