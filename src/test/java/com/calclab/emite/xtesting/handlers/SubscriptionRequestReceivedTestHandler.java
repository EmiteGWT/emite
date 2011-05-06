package com.calclab.emite.xtesting.handlers;

import com.calclab.emite.im.client.roster.events.SubscriptionRequestReceivedEvent;
import com.calclab.emite.im.client.roster.events.SubscriptionRequestReceivedHandler;

public class SubscriptionRequestReceivedTestHandler extends TestHandler<SubscriptionRequestReceivedEvent> implements SubscriptionRequestReceivedHandler {

	@Override
	public void onSubscriptionRequestReceived(SubscriptionRequestReceivedEvent event) {
		addEvent(event);
	}

}
