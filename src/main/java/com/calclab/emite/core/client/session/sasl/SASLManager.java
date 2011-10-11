package com.calclab.emite.core.client.session.sasl;

import com.calclab.emite.core.client.events.AuthorizationResultEvent;
import com.calclab.emite.core.client.session.Credentials;
import com.google.web.bindery.event.shared.HandlerRegistration;

public interface SASLManager {

	HandlerRegistration addAuthorizationResultHandler(AuthorizationResultEvent.Handler handler);
	void sendAuthorizationRequest(Credentials credentials);
	
}
