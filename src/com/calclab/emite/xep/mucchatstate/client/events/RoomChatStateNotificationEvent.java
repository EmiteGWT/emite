package com.calclab.emite.xep.mucchatstate.client.events;

import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.xep.chatstate.client.ChatStateManager.ChatState;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class RoomChatStateNotificationEvent extends GwtEvent<RoomChatStateNotificationHandler> {

    private static final Type<RoomChatStateNotificationHandler> TYPE = new Type<RoomChatStateNotificationHandler>();

    public static HandlerRegistration bind(EmiteEventBus eventBus, RoomChatStateNotificationHandler handler) {
	return eventBus.addHandler(TYPE, handler);
    }
    private final XmppURI from;

    private final ChatState chatState;

    public RoomChatStateNotificationEvent(XmppURI from, ChatState chatState) {
	this.from = from;
	this.chatState = chatState;
    }

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<RoomChatStateNotificationHandler> getAssociatedType() {
	return TYPE;
    }

    public ChatState getChatState() {
	return chatState;
    }

    public XmppURI getFrom() {
	return from;
    }

    @Override
    protected void dispatch(RoomChatStateNotificationHandler handler) {
	handler.onRoomChatStateNotification(this);
    }

}
