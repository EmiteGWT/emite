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

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.Nullable;

import com.calclab.emite.base.xml.XMLPacket;
import com.calclab.emite.core.IQCallback;
import com.calclab.emite.core.XmppURI;
import com.calclab.emite.core.conn.ConnectionStatus;
import com.calclab.emite.core.conn.XmppConnection;
import com.calclab.emite.core.conn.bosh.StreamSettings;
import com.calclab.emite.core.events.AuthorizationResultEvent;
import com.calclab.emite.core.events.BeforeStanzaSentEvent;
import com.calclab.emite.core.events.ConnectionStatusChangedEvent;
import com.calclab.emite.core.events.IQReceivedEvent;
import com.calclab.emite.core.events.MessageReceivedEvent;
import com.calclab.emite.core.events.PacketReceivedEvent;
import com.calclab.emite.core.events.PresenceReceivedEvent;
import com.calclab.emite.core.events.SessionStatusChangedEvent;
import com.calclab.emite.core.events.StanzaSentEvent;
import com.calclab.emite.core.session.sasl.SASLManager;
import com.calclab.emite.core.stanzas.IQ;
import com.calclab.emite.core.stanzas.Message;
import com.calclab.emite.core.stanzas.Presence;
import com.calclab.emite.core.stanzas.Stanza;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * Default XmppSession logic implementation. You should use XmppSession
 * interface instead.
 * 
 * @see XmppSession
 */
@Singleton
public class XmppSessionImpl implements XmppSession, ConnectionStatusChangedEvent.Handler, AuthorizationResultEvent.Handler, PacketReceivedEvent.Handler {

	private static final Logger logger = Logger.getLogger(XmppSessionImpl.class.getName());

	private final EventBus eventBus;
	private final XmppConnection connection;
	private final SASLManager saslManager;

	private final Map<String, IQCallback> iqHandlers;
	private final List<Stanza> queuedStanzas;
	private SessionStatus status;
	
	@Nullable private Credentials credentials;
	@Nullable private XmppURI userUri;
	private int iqId;

	@Inject
	protected XmppSessionImpl(@Named("emite") final EventBus eventBus, final XmppConnection connection, final SASLManager saslManager) {
		this.eventBus = checkNotNull(eventBus);
		this.connection = checkNotNull(connection);
		this.saslManager = checkNotNull(saslManager);

		iqHandlers = Maps.newHashMap();
		queuedStanzas = Lists.newArrayList();
		status = SessionStatus.disconnected;

		connection.addConnectionStatusChangedHandler(this);
		connection.addStanzaReceivedHandler(this);
		saslManager.addAuthorizationResultHandler(this);
	}

	@Override
	public void onConnectionStatusChanged(final ConnectionStatusChangedEvent event) {
		if (event.is(ConnectionStatus.error)) {
			logger.severe("Connection error: " + event.getDescription());
			setStatus(SessionStatus.error);
		} else if (event.is(ConnectionStatus.disconnected)) {
			setStatus(SessionStatus.disconnected);
		}
	}

	@Override
	public void onPacketReceived(final PacketReceivedEvent event) {
		final XMLPacket stanza = event.getPacket();
		final String name = stanza.getTagName();
		if ("message".equals(name)) {
			eventBus.fireEventFromSource(new MessageReceivedEvent(new Message(stanza)), this);
		} else if ("presence".equals(name)) {
			eventBus.fireEventFromSource(new PresenceReceivedEvent(new Presence(stanza)), this);
		} else if ("iq".equals(name)) {
			final String type = stanza.getAttribute("type");
			if ("get".equals(type) || "set".equals(type)) {
				eventBus.fireEventFromSource(new IQReceivedEvent(new IQ(stanza)), this);
			} else {
				final IQCallback handler = iqHandlers.remove(stanza.getAttribute("id"));
				if (handler == null)
					return;

				if (IQ.isSuccess(stanza)) {
					handler.onIQSuccess(new IQ(stanza));
				} else {
					handler.onIQFailure(new IQ(stanza));
				}
			}
		} else if (credentials != null && ("stream:features".equals(name) || "features".equals(name)) && stanza.hasChild("mechanisms")) {
			setStatus(SessionStatus.connecting);
			saslManager.sendAuthorizationRequest(credentials);
			credentials = null;
		}
	}

	@Override
	public void onAuthorizationResult(final AuthorizationResultEvent event) {
		if (event.isSuccess()) {
			setStatus(SessionStatus.authorized);
			connection.restartStream();
			bindResource(event.getXmppUri().getResource());
		} else {
			setStatus(SessionStatus.notAuthorized);
			connection.disconnect();
		}
	}

	@Override
	public HandlerRegistration addBeforeStanzaSentHandler(final BeforeStanzaSentEvent.Handler handler) {
		return eventBus.addHandlerToSource(BeforeStanzaSentEvent.TYPE, this, handler);
	}

	@Override
	public HandlerRegistration addIQReceivedHandler(final IQReceivedEvent.Handler handler) {
		return eventBus.addHandlerToSource(IQReceivedEvent.TYPE, this, handler);
	}

	@Override
	public HandlerRegistration addMessageReceivedHandler(final MessageReceivedEvent.Handler handler) {
		return eventBus.addHandlerToSource(MessageReceivedEvent.TYPE, this, handler);
	}

	@Override
	public HandlerRegistration addPresenceReceivedHandler(final PresenceReceivedEvent.Handler handler) {
		return eventBus.addHandlerToSource(PresenceReceivedEvent.TYPE, this, handler);
	}

	@Override
	public HandlerRegistration addSessionStatusChangedHandler(final SessionStatusChangedEvent.Handler handler, final boolean sendCurrent) {
		if (sendCurrent) {
			handler.onSessionStatusChanged(new SessionStatusChangedEvent(status));
		}

		return eventBus.addHandlerToSource(SessionStatusChangedEvent.TYPE, this, handler);
	}

	@Override
	public SessionStatus getStatus() {
		return status;
	}

	@Override
	public boolean isStatus(final SessionStatus expectedStatus) {
		return status.equals(expectedStatus);
	}

	@Override
	public void setStatus(final SessionStatus newStatus) {
		checkNotNull(newStatus);
		
		if (SessionStatus.isReady(newStatus)) {
			sendQueuedStanzas();
		} else if (SessionStatus.isDisconnected(newStatus)) {
			userUri = null;
		}
		if (!status.equals(newStatus)) {
			status = newStatus;
			eventBus.fireEventFromSource(new SessionStatusChangedEvent(newStatus), this);
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
		if (status == SessionStatus.disconnected) {
			setStatus(SessionStatus.connecting);
			connection.connect();
			this.credentials = credentials;
		}
	}

	@Override
	public void logout() {
		if (status != SessionStatus.disconnected && userUri != null) {
			// TODO : To be reviewed, preventing unavailable presences to be sent
			// so that only the 'terminate' is sent
			// Unavailable are handled automatically by the server
			setStatus(SessionStatus.loggingOut);
			userUri = null;
			connection.disconnect();
			setStatus(SessionStatus.disconnected);
		}
	}

	@Override
	public StreamSettings pause() {
		return connection.pause();
	}

	@Override
	public void resume(final XmppURI userURI, final StreamSettings settings) {
		userUri = userURI;
		setStatus(SessionStatus.resume);
		connection.resume(settings);
		setStatus(SessionStatus.ready);
	}

	@Override
	public void send(final Stanza packet) {
		send(packet, false);
	}

	private void send(final Stanza packet, final boolean force) {
		// Added a condition to check the connection is not retrying...
		if (connection.hasErrors() || userUri == null && !force) {
			logger.finer("session queuing stanza" + packet);
			queuedStanzas.add(packet);
		} else {
			if (userUri != null) {
				packet.setFrom(userUri);
			}
			eventBus.fireEventFromSource(new BeforeStanzaSentEvent(packet), this);
			connection.send(packet);
			eventBus.fireEventFromSource(new StanzaSentEvent(packet), this);
		}
	}

	@Override
	public void sendIQ(final String category, final IQ iq, final IQCallback handler) {
		sendIQ(category, iq, handler, false);
	}

	private void sendIQ(final String category, final IQ iq, final IQCallback handler, final boolean force) {
		if (!iq.getType().equals(IQ.Type.result)) {
			final String key = category + "_" + iqId++;
			iqHandlers.put(key, handler);
			iq.setId(key);
			send(iq, force);
		}
	}

	private void sendQueuedStanzas() {
		logger.finer("Sending queued stanzas...");
		for (final Stanza packet : queuedStanzas) {
			send(packet);
		}
		queuedStanzas.clear();
	}

	private void setLoggedIn(final XmppURI userURI) {
		userUri = userURI;
		logger.info("SESSION LOGGED IN");
		setStatus(SessionStatus.loggedIn);
	}

	private void bindResource(final String resource) {
		final IQ iq = new IQ(IQ.Type.set);
		iq.addChild("bind", "urn:ietf:params:xml:ns:xmpp-bind").setChildText("resource", resource);

		sendIQ("bind-resource", iq, new IQCallback() {
			@Override
			public void onIQSuccess(final IQ iq) {
				setStatus(SessionStatus.binded);
				requestSession(XmppURI.uri(iq.getChild("bind", "urn:ietf:params:xml:ns:xmpp-bind").getChildText("jid")));
			}

			@Override
			public void onIQFailure(final IQ iq) {
				connection.disconnect();
			}
		}, true);
	}

	private void requestSession(final XmppURI uri) {
		final IQ iq = new IQ(IQ.Type.set);
		iq.setFrom(uri);
		iq.setTo(uri.getHostURI());
		iq.addChild("session", "urn:ietf:params:xml:ns:xmpp-session");

		sendIQ("session-request", iq, new IQCallback() {
			@Override
			public void onIQSuccess(final IQ iq) {
				setLoggedIn(iq.getTo());
			}

			@Override
			public void onIQFailure(final IQ iq) {
				connection.disconnect();
			}
		}, true);
	}

	@Override
	public String toString() {
		return "Session " + userUri + " in " + status.toString() + " " + queuedStanzas.size() + " queued stanzas con=" + connection.toString();
	}

}
