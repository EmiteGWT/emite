package com.calclab.emite.reconnect.client;

import com.calclab.emite.core.client.bosh.Connection;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.suco.client.events.Listener;
import com.calclab.suco.client.events.Listener2;
import com.google.gwt.core.client.GWT;

public class SessionReconnect {

    private boolean shouldReconnect;

    public SessionReconnect(final Connection connection, final Session session) {
	shouldReconnect = false;
	GWT.log("RECONNECT BEHAVIOUR");
	session.onStateChanged(new Listener<Session>() {
	    @Override
	    public void onEvent(final Session session) {
		if (session.getState() == Session.State.disconnected && shouldReconnect) {
		    shouldReconnect = false;
		    connection.connect();
		}
	    }
	});

	connection.onError(new Listener<String>() {
	    @Override
	    public void onEvent(final String parameter) {
		shouldReconnect = true;
	    }
	});

	connection.onRetry(new Listener2<Integer, Integer>() {
	    @Override
	    public void onEvent(final Integer attempt, final Integer schedtime) {
		shouldReconnect = true;
	    }
	});
    }
}
