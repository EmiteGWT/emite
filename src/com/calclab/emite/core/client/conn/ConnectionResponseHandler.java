package com.calclab.emite.core.client.conn;

import com.google.gwt.event.shared.EventHandler;

public interface ConnectionResponseHandler extends EventHandler {

    void onResponse(ConnectionResponseEvent event);

}
