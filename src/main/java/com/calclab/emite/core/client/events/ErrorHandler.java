package com.calclab.emite.core.client.events;

import com.google.gwt.event.shared.EventHandler;

public interface ErrorHandler extends EventHandler {

    void onError(ErrorEvent event);

}
