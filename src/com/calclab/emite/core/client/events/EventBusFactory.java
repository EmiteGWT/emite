package com.calclab.emite.core.client.events;

/**
 * EventBuses are created using this factory. This allow us to change the
 * default event bus (for example: different loggin or testing).
 * 
 * The default implementation (GwtEventBus) doesn't work outside GWT
 * enviroments, so it should be replaced when unit testing
 * 
 */
public class EventBusFactory {

    /**
     * A simple interface to create event buses
     */
    public static interface Factory {
	public EmiteEventBus create(String eventBusName);
    }

    /**
     * The default factory creates a Gwt event bus.
     */
    private static Factory factory = new Factory() {
	@Override
	public EmiteEventBus create(String eventBusName) {
	    return new GwtEmiteEventBus(eventBusName);
	}
    };

    /**
     * Create an event bus with the given name
     * 
     * @param eventBusName
     *            the bus name: using for logging
     * @return a new event bus
     */
    public static EmiteEventBus create(String eventBusName) {
	return EventBusFactory.factory.create(eventBusName);
    }

    /**
     * Changes the event bus factory
     * 
     * @param factory
     */
    public static void setFactory(Factory factory) {
	EventBusFactory.factory = factory;
    }

}
