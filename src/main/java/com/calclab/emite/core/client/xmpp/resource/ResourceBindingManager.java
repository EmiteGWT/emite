package com.calclab.emite.core.client.xmpp.resource;

import com.google.web.bindery.event.shared.HandlerRegistration;

public interface ResourceBindingManager {

	public HandlerRegistration addResourceBindResultHandler(ResourceBindResultEvent.Handler handler);
	public void bindResource(String resource);
	
}
