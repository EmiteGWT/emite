package com.calclab.emite.xep.muc.client.subject;

import com.google.gwt.event.shared.EventHandler;

public interface RoomSubjectChangedHandler extends EventHandler {

    void onSubjectChanged(RoomSubjectChangedEvent event);

}
