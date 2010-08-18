package com.calclab.emite.xxamples.pingpong.client;

import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.core.client.events.GwtEmiteEventBus;
import com.calclab.emite.xxamples.pingpong.client.PingPongDisplay.Style;
import com.calclab.suco.client.Suco;
import com.calclab.suco.client.ioc.Provider;
import com.calclab.suco.client.ioc.decorator.Singleton;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;

public class EventBusEventsSupervisor extends GwtEmiteEventBus {

    private final PingPongDisplay display;

    public EventBusEventsSupervisor(final PingPongDisplay display) {
	this.display = display;
	Suco.getComponents().removeProvider(EmiteEventBus.class);
	Suco.getComponents().registerProvider(Singleton.instance, EmiteEventBus.class, new Provider<EmiteEventBus>() {
	    @Override
	    public EmiteEventBus get() {
		return EventBusEventsSupervisor.this;
	    }
	});

    }

    @Override
    public void fireEvent(final GwtEvent<?> event) {
	DeferredCommand.addCommand(new Command() {
	    @Override
	    public void execute() {
		display.print(event.toDebugString(), Style.eventBus);
		EventBusEventsSupervisor.super.fireEvent(event);
	    }
	});
    }

}
