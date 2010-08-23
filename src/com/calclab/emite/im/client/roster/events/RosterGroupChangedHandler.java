package com.calclab.emite.im.client.roster.events;

import com.google.gwt.event.shared.EventHandler;

public interface RosterGroupChangedHandler extends EventHandler {

    void onGroupChanged(RosterGroupChangedEvent event);

}
