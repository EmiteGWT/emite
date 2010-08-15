package com.calclab.emite.im.client.chat.events;

import com.calclab.emite.core.client.events.ChangedEvent;
import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.im.client.chat.Chat;
import com.google.gwt.event.shared.HandlerRegistration;

public class ChatChangedEvent extends ChangedEvent<ChatChangedHandler> {

    private static final Type<ChatChangedHandler> TYPE = new Type<ChatChangedHandler>();

    public static HandlerRegistration bind(final EmiteEventBus eventBus, final ChatChangedHandler handler) {
	return eventBus.addHandler(TYPE, handler);
    }

    private final Chat chat;

    public ChatChangedEvent(final String changeType, final Chat chat) {
	super(TYPE, changeType);
	assert chat != null : "Chat can't be null in ChatChangedEvent";
	this.chat = chat;
    }

    public Chat getChat() {
	return chat;
    }

    @Override
    protected void dispatch(final ChatChangedHandler handler) {
	handler.onChatChanged(this);
    }

}
