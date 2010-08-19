package com.calclab.emite.core.client.events;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;

public class GwtEmiteEventBus extends HandlerManager implements EmiteEventBus {

    private final String eventBusName;

    GwtEmiteEventBus(String eventBusName) {
	super(null);
	this.eventBusName = eventBusName;
	GWT.log("New EventBus: " + eventBusName);
    }

    @Override
    public void fireEvent(final GwtEvent<?> event) {
	DeferredCommand.addCommand(new Command() {
	    @Override
	    public void execute() {
		GWT.log("[" + eventBusName + "] " + event.toDebugString());
		GwtEmiteEventBus.super.fireEvent(event);
	    }
	});
    }

}
