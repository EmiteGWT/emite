package com.calclab.emite.core.client.events;

import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.google.gwt.event.shared.GwtEvent;

public abstract class MessageEvent extends GwtEvent<MessageHandler> {
    private final Message message;
    private final Type<MessageHandler> type;

    public MessageEvent(final Type<MessageHandler> type, final Message message) {
	this.type = type;
	this.message = message;
    }

    @Override
    public Type<MessageHandler> getAssociatedType() {
	return type;
    }

    public Message getMessage() {
	return message;
    }

    @Override
    protected void dispatch(final MessageHandler handler) {
	handler.onPacketEvent(this);
    }

}
