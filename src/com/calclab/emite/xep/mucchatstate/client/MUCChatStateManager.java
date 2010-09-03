package com.calclab.emite.xep.mucchatstate.client;

import com.calclab.emite.xep.muc.client.RoomManager;
import com.google.inject.Inject;

public class MUCChatStateManager extends MucChatStateManager {

    @Inject
    public MUCChatStateManager(RoomManager chatManager) {
	super(chatManager);
    }

}
