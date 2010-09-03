package com.calclab.emite.xep.vcard.client;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules(VCardModule.class)
public interface VCardGinjector extends Ginjector {
    VCardManager getVCardManager();
}
