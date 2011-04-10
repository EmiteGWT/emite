package com.calclab.emite.xxamples.core.xmpp.session.client;

import com.calclab.emite.browser.client.BrowserModule;
import com.calclab.emite.core.client.CoreModule;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules({CoreModule.class, BrowserModule.class})
public interface ExampleXmppSessionGinjector extends Ginjector {
    XmppSession getXmppSession();
}
