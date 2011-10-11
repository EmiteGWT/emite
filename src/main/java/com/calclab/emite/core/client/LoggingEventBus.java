package com.calclab.emite.core.client;

import java.util.logging.Logger;

import com.google.web.bindery.event.shared.Event;
import com.google.web.bindery.event.shared.SimpleEventBus;

// FIXME: For testing only. To be removed.
public class LoggingEventBus extends SimpleEventBus {

	private static final Logger logger = Logger.getLogger("EventBus");
	
	@Override
	public void fireEvent(Event<?> event) {
		logger.info("FIRE: "+event.toDebugString());
		super.fireEvent(event);
	}
	
	@Override
	public void fireEventFromSource(Event<?> event, Object source) {
		logger.info("FIRE|"+source.toString()+": " + event.toDebugString());
		super.fireEventFromSource(event, source);
	}
}
