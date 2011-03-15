package com.calclab.emite.xep.privacylists.client;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules(PrivacyListsModule.class)
public interface PrivacyListsGinjector extends Ginjector {
    PrivacyListsManager getPrivacyListsManager();
}
