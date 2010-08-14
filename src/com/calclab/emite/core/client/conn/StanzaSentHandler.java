package com.calclab.emite.core.client.conn;

import com.google.gwt.event.shared.EventHandler;

public interface StanzaSentHandler extends EventHandler {

    void onStanzaSent(StanzaSentEvent event);

}
