package com.calclab.emite.xxamples.pingpong.client;

import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.xxamples.pingpong.client.PingPongDisplay.Style;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;

public class DisplayEventBus extends HandlerManager implements EmiteEventBus {

    private final PingPongDisplay display;
    private final String eventBusName;

    public DisplayEventBus(String eventBusName, PingPongDisplay display) {
	super(null);
	this.eventBusName = eventBusName;
	this.display = display;
	display.print("New event bus: " + eventBusName, Style.eventBus);
    }

    @Override
    public void fireEvent(final GwtEvent<?> event) {
	DeferredCommand.addCommand(new Command() {
	    @Override
	    public void execute() {
		display.print("[" + eventBusName + "] " + event.toDebugString(), Style.eventBus);
		DisplayEventBus.super.fireEvent(event);
	    }
	});
    }

}
