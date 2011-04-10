package com.calclab.emite.xxamples.im.echo.client;

import com.calclab.emite.browser.client.BrowserModule;
import com.calclab.emite.core.client.CoreModule;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.calclab.emite.im.client.ImModule;
import com.calclab.emite.im.client.chat.ChatManager;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules({CoreModule.class, ImModule.class, BrowserModule.class})
interface EchoGinjector extends Ginjector {
    XmppSession getXmppSession();
    ChatManager getChatManager();
}