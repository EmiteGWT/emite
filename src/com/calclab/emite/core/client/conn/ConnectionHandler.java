package com.calclab.emite.core.client.conn;

import com.google.gwt.event.shared.EventHandler;

public interface ConnectionHandler extends EventHandler {

    void onStateChanged(ConnectionEvent event);

}
