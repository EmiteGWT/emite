package com.calclab.emite.xep.vcard.client.events;

import com.google.gwt.event.shared.EventHandler;

public interface VCardResponseHandler extends EventHandler {

    void onVCardResponse(VCardResponseEvent event);

}
