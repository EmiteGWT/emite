package com.calclab.emite.xep.avatar.client.events;

import com.google.gwt.event.shared.EventHandler;

public interface AvatarVCardHandler extends EventHandler {

    void onAvatarVCard(AvatarVCardReceivedEvent event);

}
