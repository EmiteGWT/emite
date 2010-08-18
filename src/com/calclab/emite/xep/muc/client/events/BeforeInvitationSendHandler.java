package com.calclab.emite.xep.muc.client.events;

import com.google.gwt.event.shared.EventHandler;

public interface BeforeInvitationSendHandler extends EventHandler {

    void onBeforeInvitationSend(BeforeInvitationSendEvent event);

}
