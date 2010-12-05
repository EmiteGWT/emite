package com.calclab.emite.reconnect.client;

import com.calclab.emite.core.client.conn.ConnectionStateChangedEvent;
import com.calclab.emite.core.client.conn.ConnectionStateChangedHandler;
import com.calclab.emite.core.client.conn.XmppConnection;
import com.calclab.emite.core.client.conn.ConnectionStateChangedEvent.ConnectionState;
import com.calclab.emite.core.client.events.StateChangedEvent;
import com.calclab.emite.core.client.events.StateChangedHandler;
import com.calclab.emite.core.client.xmpp.sasl.AuthorizationResultEvent;
import com.calclab.emite.core.client.xmpp.sasl.AuthorizationResultHandler;
import com.calclab.emite.core.client.xmpp.sasl.SASLManager;
import com.calclab.emite.core.client.xmpp.session.Credentials;
import com.calclab.emite.core.client.xmpp.session.SessionStates;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.inject.Inject;

public class SessionReconnect {
    private boolean shouldReconnect;
    private Credentials lastSuccessfulCredentials;
    protected int reconnectionAttempts;

    @Inject
    public SessionReconnect(final XmppConnection connection, final XmppSession session, final SASLManager saslManager) {
	shouldReconnect = false;
	reconnectionAttempts = 0;
	GWT.log("RECONNECT BEHAVIOUR");

	saslManager.addAuthorizationResultHandler(new AuthorizationResultHandler() {
	    @Override
	    public void onAuthorization(AuthorizationResultEvent event) {
		lastSuccessfulCredentials = event.getCredentials();
	    }
	});

	session.addSessionStateChangedHandler(true, new StateChangedHandler() {
	    @Override
	    public void onStateChanged(StateChangedEvent event) {
		if (event.is(SessionStates.connecting)) {
		    shouldReconnect = false;
		} else if (event.is(SessionStates.disconnected) && shouldReconnect) {
		    if (lastSuccessfulCredentials != null) {
			final double seconds = Math.pow(2, reconnectionAttempts - 1);
			new Timer() {
			    @Override
			    public void run() {
				GWT.log("Reconnecting...");
				if (shouldReconnect) {
				    shouldReconnect = false;
				    session.login(lastSuccessfulCredentials);
				}
			    }
			}.schedule((int) (1000 * seconds));
			GWT.log("Reconnecting in " + seconds + " seconds.");
		    }
		} else if (event.is(SessionStates.ready)) {
		    GWT.log("CLEAR RECONNECTION ATTEMPS");
		    reconnectionAttempts = 0;
		}
	    }
	});

	connection.addConnectionStateChangedHandler(new ConnectionStateChangedHandler() {
	    @Override
	    public void onStateChanged(ConnectionStateChangedEvent event) {
		if (event.is(ConnectionState.error) || event.is(ConnectionState.waitingForRetry)) {
		    shouldReconnect();
		}
	    }
	});

    }

    private void shouldReconnect() {
	shouldReconnect = true;
	reconnectionAttempts++;
    }
}
