/*
 * ((e)) emite: A pure Google Web Toolkit XMPP library
 * Copyright (c) 2008-2011 The Emite development team
 * 
 * This file is part of Emite.
 *
 * Emite is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * Emite is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with Emite.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.calclab.emite.reconnect.client;

import java.util.logging.Logger;

import com.calclab.emite.core.client.conn.ConnectionState;
import com.calclab.emite.core.client.conn.ConnectionStateChangedEvent;
import com.calclab.emite.core.client.conn.XmppConnection;
import com.calclab.emite.core.client.xmpp.sasl.AuthorizationResultEvent;
import com.calclab.emite.core.client.xmpp.sasl.SASLManager;
import com.calclab.emite.core.client.xmpp.session.Credentials;
import com.calclab.emite.core.client.xmpp.session.SessionState;
import com.calclab.emite.core.client.xmpp.session.SessionStateChangedEvent;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.google.gwt.user.client.Timer;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class SessionReconnect implements ConnectionStateChangedEvent.Handler, SessionStateChangedEvent.Handler, AuthorizationResultEvent.Handler {
	
	private static final Logger logger = Logger.getLogger(SessionReconnect.class.getName());
	
	private final XmppSession session;
	
	private boolean shouldReconnect;
	private Credentials lastSuccessfulCredentials;
	protected int reconnectionAttempts;

	@Inject
	public SessionReconnect(final XmppConnection connection, final XmppSession session, final SASLManager saslManager) {
		this.session = session;
		
		shouldReconnect = false;
		reconnectionAttempts = 0;
		logger.info("RECONNECT BEHAVIOUR");

		saslManager.addAuthorizationResultHandler(this);
		session.addSessionStateChangedHandler(true, this);
		connection.addConnectionStateChangedHandler(this);
	}
	
	@Override
	public void onConnectionStateChanged(final ConnectionStateChangedEvent event) {
		if (event.is(ConnectionState.error) || event.is(ConnectionState.waitingForRetry)) {
			shouldReconnect = true;
			reconnectionAttempts++;
		}
	}
	
	@Override
	public void onSessionStateChanged(final SessionStateChangedEvent event) {
		if (event.is(SessionState.connecting)) {
			shouldReconnect = false;
		} else if (event.is(SessionState.disconnected) && shouldReconnect) {
			if (lastSuccessfulCredentials != null) {
				final double seconds = Math.pow(2, reconnectionAttempts - 1);
				new Timer() {
					@Override
					public void run() {
						logger.info("Reconnecting...");
						if (shouldReconnect) {
							shouldReconnect = false;
							session.login(lastSuccessfulCredentials);
						}
					}
				}.schedule((int) (1000 * seconds));
				logger.info("Reconnecting in " + seconds + " seconds.");
			}
		} else if (event.is(SessionState.ready)) {
			logger.finer("CLEAR RECONNECTION ATTEMPS");
			reconnectionAttempts = 0;
		}
	}
	
	@Override
	public void onAuthorizationResult(final AuthorizationResultEvent event) {
		if (event.isSuccess()) {
			lastSuccessfulCredentials = event.getCredentials();
		}
	}

}
