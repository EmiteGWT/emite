package com.calclab.emite.core.client.xmpp.session;

import com.google.gwt.event.shared.EventHandler;

public interface SessionRequestResultHandler extends EventHandler {

    void onSessionRequestResult(SessionRequestResultEvent event);

}
