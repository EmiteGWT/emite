package com.calclab.emite.reconnect.client;

import com.calclab.emite.core.client.conn.Connection;
import com.calclab.emite.core.client.xmpp.sasl.AuthorizationTransaction;
import com.calclab.emite.core.client.xmpp.sasl.SASLManager;
import com.calclab.emite.core.client.xmpp.session.Credentials;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.session.Session.State;
import com.calclab.suco.client.events.Listener;
import com.calclab.suco.client.events.Listener2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;

public class SessionReconnect {
    private boolean shouldReconnect;
    private Credentials lastSuccessfulCredentials;
    protected int reconnectionAttempts;

    public SessionReconnect(final Connection connection, final Session session, final SASLManager saslManager) {
	shouldReconnect = false;
	reconnectionAttempts = 0;
	GWT.log("RECONNECT BEHAVIOUR");

	saslManager.onAuthorized(new Listener<AuthorizationTransaction>() {

	    @Override
	    public void onEvent(final AuthorizationTransaction authorizationTransaction) {
		lastSuccessfulCredentials = authorizationTransaction.getCredentials();
	    }
	});

	session.onStateChanged(new Listener<Session>() {
	    @Override
	    public void onEvent(final Session session) {
		final State sessionState = session.getState();
		if (sessionState == Session.State.connecting) {
		    shouldReconnect = false;
		} else if (sessionState == Session.State.disconnected && shouldReconnect) {
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
		} else if (sessionState == Session.State.ready) {
		    GWT.log("CLEAR RECONNECTION ATTEMPS");
		    reconnectionAttempts = 0;
		}
	    }
	});

	connection.onError(new Listener<String>() {
	    @Override
	    public void onEvent(final String parameter) {
		shouldReconnect();
	    }

	});

	connection.onRetry(new Listener2<Integer, Integer>() {
	    @Override
	    public void onEvent(final Integer attempt, final Integer schedtime) {
		shouldReconnect();
	    }
	});

    }

    private void shouldReconnect() {
	shouldReconnect = true;
	reconnectionAttempts++;
    }
}
