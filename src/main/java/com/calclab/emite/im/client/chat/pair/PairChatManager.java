package com.calclab.emite.im.client.chat.pair;

import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.emite.im.client.events.PairChatChangedEvent;
import com.google.web.bindery.event.shared.HandlerRegistration;

public interface PairChatManager extends ChatManager<PairChat> {

	/**
	 * Add a handler to track chat changes. The following changes can occur from
	 * a default chat manager: created, opened, closed
	 * 
	 * @param handler
	 */
	HandlerRegistration addPairChatChangedHandler(PairChatChangedEvent.Handler handler);
	
}
