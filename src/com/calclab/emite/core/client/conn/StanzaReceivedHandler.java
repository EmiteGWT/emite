package com.calclab.emite.core.client.conn;

import com.google.gwt.event.shared.EventHandler;

public interface StanzaReceivedHandler extends EventHandler {

    void onStanzaReceived(StanzaReceivedEvent event);

}
