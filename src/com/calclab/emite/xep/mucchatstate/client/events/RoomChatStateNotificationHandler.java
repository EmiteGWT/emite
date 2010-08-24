package com.calclab.emite.xep.mucchatstate.client.events;

import com.google.gwt.event.shared.EventHandler;

public interface RoomChatStateNotificationHandler extends EventHandler {

    void onRoomChatStateNotification(RoomChatStateNotificationEvent event);

}
