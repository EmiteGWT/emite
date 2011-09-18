package com.calclab.emite.core.client.session.sasl;

import com.calclab.emite.core.client.events.AuthorizationResultEvent;
import com.calclab.emite.core.client.session.Credentials;
import com.google.web.bindery.event.shared.HandlerRegistration;

public interface SASLManager {

	public HandlerRegistration addAuthorizationResultHandler(AuthorizationResultEvent.Handler handler);
	public void sendAuthorizationRequest(Credentials credentials);
	
}
