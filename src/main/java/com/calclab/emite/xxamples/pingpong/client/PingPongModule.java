package com.calclab.emite.xxamples.pingpong.client;

import com.calclab.emite.browser.client.PageAssist;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
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
	bind(PingPongDisplay.class).to(PingPongWidget.class).in(Singleton.class);
    }
}
