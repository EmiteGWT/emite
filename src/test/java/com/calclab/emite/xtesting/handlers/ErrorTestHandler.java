package com.calclab.emite.xtesting.handlers;

import com.calclab.emite.core.client.events.ErrorEvent;
import com.calclab.emite.core.client.events.ErrorHandler;

public class ErrorTestHandler extends TestHandler<ErrorEvent> implements ErrorHandler {

	@Override
	public void onError(final ErrorEvent event) {
		addEvent(event);
	}

}
