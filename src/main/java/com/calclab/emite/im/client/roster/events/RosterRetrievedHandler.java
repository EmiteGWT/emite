package com.calclab.emite.im.client.roster.events;

import com.google.gwt.event.shared.EventHandler;

public interface RosterRetrievedHandler extends EventHandler {

    void onRosterRetrieved(RosterRetrievedEvent event);

}
