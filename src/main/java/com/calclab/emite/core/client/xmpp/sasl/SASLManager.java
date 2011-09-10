package com.calclab.emite.core.client.xmpp.sasl;

import com.calclab.emite.core.client.xmpp.session.Credentials;
import com.google.web.bindery.event.shared.HandlerRegistration;

public interface SASLManager {

	public HandlerRegistration addAuthorizationResultHandler(AuthorizationResultEvent.Handler handler);
	public void sendAuthorizationRequest(Credentials credentials);
	
}
