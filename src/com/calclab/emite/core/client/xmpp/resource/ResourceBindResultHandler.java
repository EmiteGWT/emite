package com.calclab.emite.core.client.xmpp.resource;

import com.google.gwt.event.shared.EventHandler;

public interface ResourceBindResultHandler extends EventHandler {

    void onBinded(ResourceBindResultEvent event);

}
