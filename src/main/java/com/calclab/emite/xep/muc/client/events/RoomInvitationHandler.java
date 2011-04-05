package com.calclab.emite.xep.muc.client.events;

import com.google.gwt.event.shared.EventHandler;

public interface RoomInvitationHandler extends EventHandler {

    void onRoomInvitation(RoomInvitationEvent event);

}
