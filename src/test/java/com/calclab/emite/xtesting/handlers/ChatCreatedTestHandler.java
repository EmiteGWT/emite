package com.calclab.emite.xtesting.handlers;

import com.calclab.emite.im.client.chat.events.ChatChangedEvent;

public class ChatCreatedTestHandler extends ChatChangedTestHandler {

	@Override
	public void onChatChanged(ChatChangedEvent event) {
		if (event.isCreated())
			addEvent(event);
	}

}
