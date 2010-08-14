package com.calclab.emite.core.client.events;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;

public class DefaultEmiteEventBus extends HandlerManager implements EmiteEventBus {

    public DefaultEmiteEventBus() {
	super(null);
    }

    @Override
    public void fireEvent(final GwtEvent<?> event) {
	GWT.log(event.toDebugString());
	super.fireEvent(event);
    }

}
