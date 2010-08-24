package com.calclab.emite.xep.chatstate.client.events;

import com.google.gwt.event.shared.EventHandler;

public interface ChatStateNotificationHandler extends EventHandler {

    void onChatStateChanged(ChatStateNotificationEvent event);

}
