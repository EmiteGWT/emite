package com.calclab.emite.xep.chatstate.client;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;

public class ChatStateModule extends AbstractGinModule {

    @Override
    protected void configure() {
	bind(StateManager.class).in(Singleton.class);
	bind(ChatStateComponents.class).asEagerSingleton();
    }

}
