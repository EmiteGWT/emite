package com.calclab.emite.xtesting;

import com.calclab.emite.core.client.events.EmiteEventBus;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;

public class BasicEmiteEventBus extends HandlerManager implements EmiteEventBus {

    public BasicEmiteEventBus() {
	super(null);
    }

    @Override
    public void fireEvent(final GwtEvent<?> event) {
	GWT.log(event.toDebugString());
	super.fireEvent(event);
    }

}
