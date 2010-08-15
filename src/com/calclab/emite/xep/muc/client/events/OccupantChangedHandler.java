package com.calclab.emite.xep.muc.client.events;

import com.google.gwt.event.shared.EventHandler;

public interface OccupantChangedHandler extends EventHandler {

    void onOccupantChanged(OccupantChangedEvent event);

}
