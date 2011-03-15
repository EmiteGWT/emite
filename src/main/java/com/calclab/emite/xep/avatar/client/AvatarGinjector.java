package com.calclab.emite.xep.avatar.client;

import com.google.gwt.inject.client.GinModules;

@GinModules(AvatarModule.class)
public interface AvatarGinjector {
    AvatarManager getAvatarManager();
}
