package com.calclab.emite.xep.avatar.client;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;

public class AvatarModule extends AbstractGinModule {

    @Override
    protected void configure() {
	bind(AvatarManager.class).in(Singleton.class);
    }

}
