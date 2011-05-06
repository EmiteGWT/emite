package com.calclab.emite.xtesting.handlers;

import com.calclab.emite.im.client.chat.Chat;
import com.calclab.emite.im.client.chat.events.ChatChangedEvent;
import com.calclab.emite.im.client.chat.events.ChatChangedHandler;

public class ChatChangedTestHandler extends TestHandler<ChatChangedEvent> implements ChatChangedHandler {

	private final String type;

	public ChatChangedTestHandler() {
		this(null);
	}

	public ChatChangedTestHandler(String type) {
		this.type = type;
	}

	public Chat getLastChat() {
		return hasEvent() ? getLastEvent().getChat() : null;
	}

	@Override
	public void onChatChanged(ChatChangedEvent event) {
		if (type == null || type.equals(event.getChangeType()))
			addEvent(event);
	}

}
