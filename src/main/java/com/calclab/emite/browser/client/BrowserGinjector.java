package com.calclab.emite.browser.client;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules(BrowserModule.class)
public interface BrowserGinjector extends Ginjector {
    AutoConfig getAutoConfig();
}
