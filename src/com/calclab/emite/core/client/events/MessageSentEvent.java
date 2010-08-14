package com.calclab.emite.core.client.events;

import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.google.gwt.event.shared.HandlerRegistration;

public class MessageSentEvent extends MessageEvent {
    private static final Type<MessageHandler> TYPE = new Type<MessageHandler>();

    public static HandlerRegistration bind(final EmiteEventBus eventBus, final MessageHandler handler) {
	return eventBus.addHandler(TYPE, handler);
    }

    public static Type<MessageHandler> getType() {
	return TYPE;
    }

    public MessageSentEvent(final Message message) {
	super(TYPE, message);
    }
}
