package com.calclab.emite.xtesting.handlers;

import com.calclab.emite.core.client.events.MessageEvent;
import com.calclab.emite.core.client.events.MessageHandler;
import com.calclab.emite.core.client.xmpp.stanzas.Message;

public class MessageTestHandler extends TestHandler<MessageEvent> implements MessageHandler {

    public Message getLastMessage() {
	return hasEvent() ? getLastEvent().getMessage() : null;
    }

    @Override
    public void onMessage(final MessageEvent event) {
	setEvent(event);
    }

}
