package com.calclab.emite.xep.chatstate.client;

import com.google.gwt.inject.client.GinModules;

@GinModules(ChatStateModule.class)
public interface ChatStateGinjector {
    StateManager getStateManager();
}
