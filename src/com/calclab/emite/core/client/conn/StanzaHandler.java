package com.calclab.emite.core.client.conn;

import com.google.gwt.event.shared.EventHandler;

public interface StanzaHandler extends EventHandler {

    void onStanza(StanzaEvent event);

}
