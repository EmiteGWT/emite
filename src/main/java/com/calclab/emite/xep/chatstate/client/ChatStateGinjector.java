package com.calclab.emite.xep.chatstate.client;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules(ChatStateModule.class)
public interface ChatStateGinjector extends Ginjector {
    StateManager getStateManager();
}
