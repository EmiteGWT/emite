package com.calclab.emite.core.client.conn;

import com.google.gwt.event.shared.EventHandler;

public interface ConnectionStateChangedHandler extends EventHandler {

    void onStateChanged(ConnectionStateChangedEvent event);

}
