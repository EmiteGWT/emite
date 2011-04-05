package com.calclab.emite.core.client.xmpp.sasl;

import com.google.gwt.event.shared.EventHandler;

public interface AuthorizationResultHandler extends EventHandler {

    void onAuthorization(AuthorizationResultEvent event);

}
