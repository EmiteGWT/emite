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

package com.calclab.emite.core.client.xmpp.session;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.calclab.emite.core.client.conn.ConnectionStateChangedEvent;
import com.calclab.emite.core.client.conn.bosh.StreamSettings;
import com.calclab.emite.core.client.conn.ConnectionState;
import com.calclab.emite.core.client.conn.XmppConnection;
import com.calclab.emite.core.client.events.BeforeStanzaSentEvent;
import com.calclab.emite.core.client.events.IQReceivedEvent;
import com.calclab.emite.core.client.events.MessageReceivedEvent;
import com.calclab.emite.core.client.events.PresenceReceivedEvent;
import com.calclab.emite.core.client.events.StanzaReceivedEvent;
import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.xmpp.resource.ResourceBindResultEvent;
import com.calclab.emite.core.client.xmpp.resource.ResourceBindingManager;
import com.calclab.emite.core.client.xmpp.sasl.AuthorizationResultEvent;
import com.calclab.emite.core.client.xmpp.sasl.SASLManager;
import com.calclab.emite.core.client.xmpp.stanzas.IQ;
import com.calclab.emite.core.client.xmpp.stanzas.IQ.Type;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.Presence;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.web.bindery.event.shared.EventBus;

/**
 * Default XmppSession logic implementation. You should use XmppSession
 * interface instead.
 * 
 * @see XmppSession
 */
@Singleton
public class XmppSessionImpl extends XmppSessionBoilerplate implements ConnectionStateChangedEvent.Handler, AuthorizationResultEvent.Handler, StanzaReceivedEvent.Handler, ResourceBindResultEvent.Handler, SessionRequestResultEvent.Handler {

	private static final Logger logger = Logger.getLogger(XmppSessionImpl.class.getName());

	private final XmppConnection connection;
	private final SASLManager saslManager;
	private final ResourceBindingManager bindingManager;
	private final IMSessionManager imSessionManager;
	private final IQManager iqManager;
	
	private final ArrayList<IPacket> queuedStanzas;
	private Credentials credentials;
	private XmppURI userUri;
	
	@Inject
	public XmppSessionImpl(@Named("emite") final EventBus eventBus, final XmppConnection connection, final SASLManager saslManager, final ResourceBindingManager bindingManager,
			final IMSessionManager imSessionManager) {
		super(eventBus);
		
		this.connection = connection;
		this.saslManager = saslManager;
		this.bindingManager = bindingManager;
		this.imSessionManager = imSessionManager;
		
		iqManager = new IQManager();
		queuedStanzas = new ArrayList<IPacket>();

		connection.addConnectionStateChangedHandler(this);
		connection.addStanzaReceivedHandler(this);
		saslManager.addAuthorizationResultHandler(this);
		bindingManager.addResourceBindResultHandler(this);
		imSessionManager.addSessionRequestResultHandler(this);
	}
	
	@Override
	public void onConnectionStateChanged(final ConnectionStateChangedEvent event) {
		if (event.is(ConnectionState.error)) {
			logger.severe("Connection error: " + event.getDescription());
			setSessionState(SessionState.error);
		} else if (event.is(ConnectionState.disconnected)) {
			setSessionState(SessionState.disconnected);
		}
	}
	
	@Override
	public void onStanzaReceived(final StanzaReceivedEvent event) {
		final IPacket stanza = event.getStanza();
		final String name = stanza.getName();
		if (name.equals("message")) {
			eventBus.fireEventFromSource(new MessageReceivedEvent(new Message(stanza)), this);
		} else if (name.equals("presence")) {
			eventBus.fireEventFromSource(new PresenceReceivedEvent(new Presence(stanza)), this);
		} else if (name.equals("iq")) {
			final String type = stanza.getAttribute("type");
			if ("get".equals(type) || "set".equals(type)) {
				eventBus.fireEventFromSource(new IQReceivedEvent(new IQ(stanza)), this);
			} else {
				iqManager.handle(stanza);
			}
		} else if (credentials != null && ("stream:features".equals(name) || "features".equals(name)) && stanza.hasChild("mechanisms")) {
			setSessionState(SessionState.connecting);
			saslManager.sendAuthorizationRequest(credentials);
			credentials = null;
		}
	}
	
	@Override
	public void onAuthorizationResult(final AuthorizationResultEvent event) {
		if (event.isSuccess()) {
			setSessionState(SessionState.authorized);
			connection.restartStream();
			bindingManager.bindResource(event.getXmppUri().getResource());
		} else {
			setSessionState(SessionState.notAuthorized);
			disconnect();
		}
	}
	
	@Override
	public void onResourceBindResult(final ResourceBindResultEvent event) {
		setSessionState(SessionState.binded);
		imSessionManager.requestSession(event.getXmppUri());
	}
	
	@Override
	public void onSessionRequestResult(final SessionRequestResultEvent event) {
		if (event.isSuccess()) {
			setLoggedIn(event.getXmppUri());
		} else {
			disconnect();
		}
	}

	@Override
	public XmppURI getCurrentUserURI() {
		return userUri;
	}

	@Override
	public boolean isReady() {
		return userUri != null;
	}

	@Override
	public void login(final Credentials credentials) {
		logger.info("XmppSessionLogic - login");
		if (getSessionState() == SessionState.disconnected) {
			setSessionState(SessionState.connecting);
			connection.connect();
			this.credentials = credentials;
		}

	}

	@Override
	public void logout() {
		if (getSessionState() != SessionState.disconnected && userUri != null) {
			// TODO : To be reviewed, preventing unvailable presences to be sent
			// so that only the 'terminate' is sent
			// Unvailabel are handled automatically by the server
			setSessionState(SessionState.loggingOut);
			userUri = null;
			connection.disconnect();
			setSessionState(SessionState.disconnected);
		}
	}

	@Override
	public StreamSettings pause() {
		return connection.pause();
	}

	@Override
	public void resume(final XmppURI userURI, final StreamSettings settings) {
		userUri = userURI;
		setSessionState(SessionState.resume);
		connection.resume(settings);
		setSessionState(SessionState.ready);
	}

	@Override
	public void send(final IPacket packet) {
		// Added a condition to check the connection is not retrying...
		if (connection.hasErrors() || userUri == null) {
			logger.finer("session queuing stanza" + packet);
			queuedStanzas.add(packet);
		} else {
			packet.setAttribute("from", userUri.toString());
			eventBus.fireEventFromSource(new BeforeStanzaSentEvent(packet), this);
			connection.send(packet);
		}
	}

	@Override
	public void sendIQ(final String category, final IQ iq, final IQCallback handler) {
		if (!iq.getType().equals(Type.result)) {
			final String id = iqManager.register(category, handler);
			iq.setAttribute("id", id);
			send(iq);
		}
	}

	@Override
	public void setSessionState(final SessionState newState) {
		if (SessionState.ready.equals(newState)) {
			sendQueuedStanzas();
		} else if (SessionState.disconnected.equals(newState)) {
			userUri = null;
		}
		super.setSessionState(newState);
	}

	@Override
	public String toString() {
		return "Session " + userUri + " in " + getSessionState().toString() + " " + queuedStanzas.size() + " queued stanzas con=" + connection.toString();
	}

	private void disconnect() {
		connection.disconnect();
	}

	private void sendQueuedStanzas() {
		logger.finer("Sending queued stanzas....");
		for (final IPacket packet : queuedStanzas) {
			send(packet);
		}
		queuedStanzas.clear();
	}

	private void setLoggedIn(final XmppURI userURI) {
		userUri = userURI;
		logger.info("SESSION LOGGED IN");
		setSessionState(SessionState.loggedIn);
	}

	public XmppConnection getConnection() {
		return connection;
	}
}
