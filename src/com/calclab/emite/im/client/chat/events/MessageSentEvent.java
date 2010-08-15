package com.calclab.emite.im.client.chat.events;

import com.calclab.emite.core.client.events.DefaultEmiteEventBus;
import com.calclab.emite.core.client.events.MessageEvent;
import com.calclab.emite.core.client.events.MessageHandler;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.google.gwt.event.shared.HandlerRegistration;

public class MessageSentEvent extends MessageEvent {

    private static final Type<MessageHandler> TYPE = new Type<MessageHandler>();

    public static HandlerRegistration bind(final DefaultEmiteEventBus eventBus, final MessageHandler handler) {
	return eventBus.addHandler(TYPE, handler);
    }

    public MessageSentEvent(final Message message) {
	super(TYPE, message);
    }

}
