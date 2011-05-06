package com.calclab.emite.xtesting;

import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.core.client.events.EventBusFactory;
import com.calclab.emite.core.client.events.EventBusFactory.Factory;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;

public class EmiteTestsEventBus extends HandlerManager implements EmiteEventBus {

	private static Factory factory;

	public static EmiteEventBus create(final String eventBusName) {
		if (EmiteTestsEventBus.factory == null) {
			EmiteTestsEventBus.factory = new Factory() {
				@Override
				public EmiteEventBus create(final String eventBusName) {
					return new EmiteTestsEventBus(eventBusName);
				}
			};
			EventBusFactory.setFactory(factory);
		}
		return EventBusFactory.create(eventBusName);
	}

	private final String name;

	public EmiteTestsEventBus(final String name) {
		super(null);
		this.name = name;
		System.out.println("New event bus: " + name);
	}

	@Override
	public void fireEvent(final GwtEvent<?> event) {
		System.out.println("[" + name + "] " + event.toDebugString());
		super.fireEvent(event);
	}

}
