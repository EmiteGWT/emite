package com.calclab.emite.xtesting.handlers;

import java.util.ArrayList;

public abstract class TestHandler<T> {
    private final ArrayList<T> events;

    public TestHandler() {
	events = new ArrayList<T>();
    }

    public int getCalledTimes() {
	return events.size();
    }

    public T getEvent(int index) {
	return events.get(index);
    }

    public T getLastEvent() {
	int size = getCalledTimes();
	return size > 0 ? events.get(size - 1) : null;
    }

    public boolean hasEvent() {
	return isCalledOnce();
    }

    public boolean isCalledOnce() {
	return getCalledTimes() == 1;
    }

    public void addEvent(final T event) {
	events.add(event);
    }
}
