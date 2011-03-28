package com.calclab.emite.xep.avatar.client;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules(AvatarModule.class)
public interface AvatarGinjector extends Ginjector {
    AvatarManager getAvatarManager();
}
