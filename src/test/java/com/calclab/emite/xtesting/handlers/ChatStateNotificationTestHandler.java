package com.calclab.emite.xtesting.handlers;

import com.calclab.emite.xep.chatstate.client.ChatStateManager.ChatState;
import com.calclab.emite.xep.chatstate.client.events.ChatStateNotificationEvent;
import com.calclab.emite.xep.chatstate.client.events.ChatStateNotificationHandler;

public class ChatStateNotificationTestHandler extends TestHandler<ChatStateNotificationEvent> implements
	ChatStateNotificationHandler {
    public ChatState getLastChatState() {
	return hasEvent() ? getLastEvent().getChatState() : null;
    }

    @Override
    public void onChatStateChanged(ChatStateNotificationEvent event) {
	addEvent(event);
    }

}
