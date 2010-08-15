package com.calclab.emite.core.client.conn;

import com.google.gwt.event.shared.EventHandler;

public interface ConnectionStateHandler extends EventHandler {

    void onStateChanged(ConnectionStateEvent event);

}
