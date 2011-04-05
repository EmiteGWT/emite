package com.calclab.emite.im.client.chat.events;

import com.google.gwt.event.shared.EventHandler;

public interface ChatChangedHandler extends EventHandler {

    void onChatChanged(ChatChangedEvent event);

}
