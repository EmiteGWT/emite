package com.calclab.emite.xep.muc.client.events;

import com.google.gwt.event.shared.EventHandler;

public interface RoomInvitationSentHandler extends EventHandler {

    void onRoomInvitationSent(RoomInvitationSentEvent event);

}
