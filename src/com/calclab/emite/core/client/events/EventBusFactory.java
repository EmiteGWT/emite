package com.calclab.emite.core.client.events;

public class EventBusFactory {

    public static interface Factory {
	public EmiteEventBus create(String eventBusName);
    }

    private static Factory factory;

    public static EmiteEventBus create(String eventBusName) {
	return EventBusFactory.factory.create(eventBusName);
    }

    public static void setFactory(Factory factory) {
	EventBusFactory.factory = factory;
    }

    private EventBusFactory() {
	EventBusFactory.factory = new Factory() {
	    @Override
	    public EmiteEventBus create(String eventBusName) {
		return new GwtEmiteEventBus(eventBusName);
	    }
	};
    }
}
