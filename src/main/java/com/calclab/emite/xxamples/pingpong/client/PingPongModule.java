package com.calclab.emite.xxamples.pingpong.client;

import com.calclab.emite.browser.client.PageAssist;
import com.calclab.emite.core.client.CoreModule;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.ImModule;
import com.calclab.emite.xep.muc.client.MucModule;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

public class PingPongModule extends AbstractGinModule {

    @Provides
    @Named("other")
    public XmppURI getOtherUri() {
	return XmppURI.uri(PageAssist.getMeta("pingpong.other"));
    }

    @Provides
    @Named("room")
    public XmppURI getRoomUri() {
	return XmppURI.uri(PageAssist.getMeta("pingpong.room"));
    }

    @Override
    protected void configure() {
	install(new CoreModule());
	install(new ImModule());
	install(new MucModule());
	bind(PingPongDisplay.class).to(PingPongWidget.class).in(Singleton.class);
    }
}
