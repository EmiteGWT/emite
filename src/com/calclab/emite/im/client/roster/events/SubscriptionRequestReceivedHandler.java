package com.calclab.emite.im.client.roster.events;

import com.google.gwt.event.shared.EventHandler;

public interface SubscriptionRequestReceivedHandler extends EventHandler {

    void onSubscriptionRequestReceived(SubscriptionRequestReceivedEvent event);

}
