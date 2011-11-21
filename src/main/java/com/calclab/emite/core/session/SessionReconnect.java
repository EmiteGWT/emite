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

package com.calclab.emite.core.session;

import java.util.logging.Logger;

import com.calclab.emite.core.conn.ConnectionStatus;
import com.calclab.emite.core.conn.XmppConnection;
import com.calclab.emite.core.events.AuthorizationResultEvent;
import com.calclab.emite.core.events.ConnectionStatusChangedEvent;
import com.calclab.emite.core.events.SessionStatusChangedEvent;
import com.calclab.emite.core.sasl.Credentials;
import com.google.gwt.user.client.Timer;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public final class SessionReconnect implements ConnectionStatusChangedEvent.Handler, SessionStatusChangedEvent.Handler, AuthorizationResultEvent.Handler {

	private static final Logger logger = Logger.getLogger(SessionReconnect.class.getName());

	private final XmppSession session;

	private Credentials lastSuccessfulCredentials;
	protected int reconnectionAttempts;
	private boolean shouldReconnect;

	protected SessionReconnect(final XmppConnection connection, final XmppSession session, final SASLManager saslManager) {
		this.session = session;

		shouldReconnect = false;
		reconnectionAttempts = 0;
		logger.info("RECONNECT BEHAVIOUR");

		connection.addConnectionStatusChangedHandler(this);
		session.addSessionStatusChangedHandler(this, true);
		saslManager.addAuthorizationResultHandler(this);
	}

	@Override
	public void onConnectionStatusChanged(final ConnectionStatusChangedEvent event) {
		if (event.is(ConnectionStatus.error) || event.is(ConnectionStatus.waitingForRetry)) {
			shouldReconnect = true;
			reconnectionAttempts++;
		}
	}

	@Override
	public void onSessionStatusChanged(final SessionStatusChangedEvent event) {
		if (SessionStatus.connecting.equals(event.getStatus())) {
			shouldReconnect = false;
		} else if (SessionStatus.isDisconnected(event.getStatus()) && shouldReconnect) {
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
		} else if (SessionStatus.isReady(event.getStatus())) {
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
