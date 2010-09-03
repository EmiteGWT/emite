package com.calclab.emite.xep.mucchatstate.client;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules(MucChatStateModule.class)
public interface MucChatStateGinjector extends Ginjector {
    MucChatStateManager getMucChatStateManager();

    @Deprecated
    MUCChatStateManager getMUCChatStateManager();
}
