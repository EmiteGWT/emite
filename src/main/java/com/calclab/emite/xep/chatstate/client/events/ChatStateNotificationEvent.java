package com.calclab.emite.xep.chatstate.client.events;

import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.xep.chatstate.client.ChatStateManager.ChatState;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class ChatStateNotificationEvent extends GwtEvent<ChatStateNotificationHandler> {
    private static final Type<ChatStateNotificationHandler> TYPE = new Type<ChatStateNotificationHandler>();

    public static HandlerRegistration bind(EmiteEventBus eventBus, ChatStateNotificationHandler handler) {
	return eventBus.addHandler(TYPE, handler);
    }

    private final ChatState chatState;

    public ChatStateNotificationEvent(ChatState chatState) {
	this.chatState = chatState;
    }

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<ChatStateNotificationHandler> getAssociatedType() {
	return TYPE;
    }

    public ChatState getChatState() {
	return chatState;
    }

    @Override
    protected void dispatch(ChatStateNotificationHandler handler) {
	handler.onChatStateChanged(this);
    }

}
