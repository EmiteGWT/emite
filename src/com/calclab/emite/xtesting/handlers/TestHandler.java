package com.calclab.emite.xtesting.handlers;

public abstract class TestHandler<T> {

    protected T event;

    public TestHandler() {
	event = null;
    }

    public T getEvent() {
	return event;
    }

    public boolean hasEvent() {
	return event != null;
    }

    public boolean isCalledOnce() {
	return hasEvent();
    }

    public void setEvent(final T event) {
	this.event = event;
    }
}
