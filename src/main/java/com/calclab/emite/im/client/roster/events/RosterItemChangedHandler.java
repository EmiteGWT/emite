package com.calclab.emite.im.client.roster.events;

import com.google.gwt.event.shared.EventHandler;

public interface RosterItemChangedHandler extends EventHandler {

    void onRosterItemChanged(RosterItemChangedEvent event);

}
