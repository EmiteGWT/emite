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
import java.util.logging.Logger;

import javax.annotation.Nullable;

import com.calclab.emite.base.util.Base64;
import com.calclab.emite.base.xml.XMLBuilder;
import com.calclab.emite.base.xml.XMLPacket;
import com.calclab.emite.core.IQCallback;
import com.calclab.emite.core.XmppNamespaces;
import com.calclab.emite.core.XmppURI;
import com.calclab.emite.core.conn.ConnectionStatus;
import com.calclab.emite.core.conn.StreamSettings;
import com.calclab.emite.core.conn.XmppConnection;
import com.calclab.emite.core.events.AuthorizationResultEvent;
import com.calclab.emite.core.events.ConnectionStatusChangedEvent;
import com.calclab.emite.core.events.IQRequestReceivedEvent;
import com.calclab.emite.core.events.IQResponseReceivedEvent;
import com.calclab.emite.core.events.MessageReceivedEvent;
import com.calclab.emite.core.events.PacketReceivedEvent;
import com.calclab.emite.core.events.PresenceReceivedEvent;
import com.calclab.emite.core.events.SessionStatusChangedEvent;
import com.calclab.emite.core.events.StanzaSentEvent;
import com.calclab.emite.core.sasl.Credentials;
import com.calclab.emite.core.sasl.PlainClient;
import com.calclab.emite.core.sasl.SaslClient;
import com.calclab.emite.core.sasl.SaslException;
import com.calclab.emite.core.sasl.ScramSHA1Client;
import com.calclab.emite.core.stanzas.IQ;
import com.calclab.emite.core.stanzas.Message;
import com.calclab.emite.core.stanzas.Presence;
import com.calclab.emite.core.stanzas.Stanza;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
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
public class XmppSessionImpl implements XmppSession, ConnectionStatusChangedEvent.Handler, PacketReceivedEvent.Handler {

	private static enum SessionMode {
		// TODO
		offline, login, register, ready;
	}
	
	private static final Logger logger = Logger.getLogger(XmppSessionImpl.class.getName());

	private final EventBus eventBus;
	private final XmppConnection connection;
	
	private final IQManager iqManager;

	private final List<Stanza> queuedStanzas;
	private SessionStatus status;
	private SessionMode mode;
	private SaslClient saslClient;
	
	@Nullable private Credentials credentials;
	@Nullable private XmppURI userUri;

	@Inject
	protected XmppSessionImpl(@Named("emite") final EventBus eventBus, final XmppConnection connection) {
		this.eventBus = checkNotNull(eventBus);
		this.connection = checkNotNull(connection);
		
		queuedStanzas = Lists.newArrayList();
		status = SessionStatus.disconnected;
		mode = SessionMode.offline;
		
		iqManager = new IQManager(this);
		new SessionReady(this);

		connection.addConnectionStatusChangedHandler(this);
		connection.addPacketReceivedHandler(this);
	}

	@Override
	public void onConnectionStatusChanged(final ConnectionStatusChangedEvent event) {
		if (event.is(ConnectionStatus.error)) {
			logger.severe("Connection error: " + event.getDescription());
			setStatus(SessionStatus.error);
			mode = SessionMode.offline;
		} else if (event.is(ConnectionStatus.disconnected)) {
			setStatus(SessionStatus.disconnected);
			mode = SessionMode.offline;
		}
	}

	@Override
	public void onPacketReceived(final PacketReceivedEvent event) {
		final XMLPacket stanza = event.getPacket();
		final String name = stanza.getTagName();
		final String xmlns = stanza.getNamespace();
		
		if (mode == SessionMode.login) {
			if (("stream:features".equals(name) || "features".equals(name)) && stanza.hasChild("mechanisms", XmppNamespaces.SASL)) {
				setStatus(SessionStatus.connecting);
				
				List<String> mechanisms = Lists.newArrayList();
				for (final XMLPacket mechanism : stanza.getFirstChild("mechanisms", XmppNamespaces.SASL).getChildren("mechanism")) {
					mechanisms.add(mechanism.getText());
				}
				
				if (credentials.isAnoymous()) {
					if (!mechanisms.contains("ANONYMOUS")) {
						setStatus(SessionStatus.notAuthorized);
						mode = SessionMode.offline;
						connection.disconnect();
					}
					
					connection.send(XMLBuilder.create("auth", XmppNamespaces.SASL).attribute("mechanism", "ANONYMOUS").getXML());
					return;
				}
				
				if (mechanisms.contains("SCRAM-SHA-1")) {
					saslClient = new ScramSHA1Client(credentials);
				}
				else if (mechanisms.contains("PLAIN")) {
					saslClient = new PlainClient(credentials);
				}
				else {
					setStatus(SessionStatus.notAuthorized);
					mode = SessionMode.offline;
					connection.disconnect();
				}
				
				final String encondedAuth = Base64.toBase64(saslClient.getInitialResponse());
				connection.send(XMLBuilder.create("auth", XmppNamespaces.SASL).attribute("mechanism", saslClient.getMechanismName()).text(encondedAuth).getXML());
			} else if ("challenge".equals(name) && XmppNamespaces.SASL.equals(xmlns)) {
				final byte[] challenge = Base64.fromBase64(stanza.getText());
				try {
					final String encondedAuth = Base64.toBase64(saslClient.evaluateChallenge(challenge));
					connection.send(XMLBuilder.create("response", XmppNamespaces.SASL).text(encondedAuth).getXML());
				} catch (SaslException e) {
					setStatus(SessionStatus.notAuthorized);
					mode = SessionMode.offline;
					connection.disconnect();
				}
			} else if ("success".equals(name) && XmppNamespaces.SASL.equals(xmlns)) {
				final byte[] challenge = Base64.fromBase64(stanza.getText());
				if (challenge != null && challenge.length > 0) {
					try {
						saslClient.evaluateChallenge(challenge);
					} catch (SaslException e) {
						setStatus(SessionStatus.notAuthorized);
						mode = SessionMode.offline;
						connection.disconnect();
						return;
					}
				}
				setStatus(SessionStatus.authorized);
				mode = SessionMode.ready;
				eventBus.fireEventFromSource(new AuthorizationResultEvent(credentials), this);
				connection.restartStream();
				bindResource(credentials.isAnoymous() ? null : credentials.getURI().getResource());
			} else if ("failure".equals(name) && XmppNamespaces.SASL.equals(xmlns)) {
				setStatus(SessionStatus.notAuthorized);
				mode = SessionMode.offline;
				connection.disconnect();
			}
		} else {
			if ("message".equals(name)) {
				eventBus.fireEventFromSource(new MessageReceivedEvent(new Message(stanza)), this);
			} else if ("presence".equals(name)) {
				eventBus.fireEventFromSource(new PresenceReceivedEvent(new Presence(stanza)), this);
			} else if ("iq".equals(name)) {
				final IQ iq = new IQ(stanza);
				final IQ.Type type = iq.getType();
				if (IQ.Type.get.equals(type) || IQ.Type.set.equals(type)) {
					eventBus.fireEventFromSource(new IQRequestReceivedEvent(iq), this);
				} else if (IQ.Type.result.equals(type) || IQ.Type.error.equals(type)) {
					eventBus.fireEventFromSource(new IQResponseReceivedEvent(iq), this);
				}
			}
		}
	}

	@Override
	public HandlerRegistration addIQRequestReceivedHandler(final IQRequestReceivedEvent.Handler handler) {
		return eventBus.addHandlerToSource(IQRequestReceivedEvent.TYPE, this, handler);
	}
	
	@Override
	public HandlerRegistration addIQResponseReceivedHandler(final IQResponseReceivedEvent.Handler handler) {
		return eventBus.addHandlerToSource(IQResponseReceivedEvent.TYPE, this, handler);
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
	public HandlerRegistration addAuthorizationResultHandler(final AuthorizationResultEvent.Handler handler) {
		return eventBus.addHandlerToSource(AuthorizationResultEvent.TYPE, this, handler);
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
	public void setStatus(final SessionStatus newStatus) {
		checkNotNull(newStatus);
		
		if (SessionStatus.isReady(newStatus)) {
			logger.finer("Sending queued stanzas...");
			for (final Stanza packet : queuedStanzas) {
				send(packet, true);
			}
			queuedStanzas.clear();
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
		checkNotNull(credentials);
		
		if (status == SessionStatus.disconnected) {
			setStatus(SessionStatus.connecting);
			mode = SessionMode.login;
			this.credentials = credentials;
			connection.connect();
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
			mode = SessionMode.offline;
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
		mode = SessionMode.ready;
	}

	@Override
	public void send(final Stanza stanza) {
		send(stanza, false);
	}

	protected void send(final Stanza stanza, final boolean force) {
		// Added a condition to check the connection is not retrying...
		if (connection.hasErrors() || userUri == null && !force) {
			logger.finer("session queuing stanza" + stanza);
			queuedStanzas.add(stanza);
			return;
		}
		
		if (userUri != null) {
			stanza.setFrom(userUri);
		}
		connection.send(stanza);
		eventBus.fireEventFromSource(new StanzaSentEvent(stanza), this);
	}

	@Override
	public void sendIQ(final String category, final IQ iq, final IQCallback handler) {
		iqManager.sendIQRequest(category, iq, handler, false);
	}

	private void bindResource(@Nullable final String resource) {
		final IQ iq = new IQ(IQ.Type.set);
		
		if (Strings.isNullOrEmpty(resource))
			iq.addExtension("bind", XmppNamespaces.BIND);
		else
			iq.addExtension("bind", XmppNamespaces.BIND).setChildText("resource", resource);

		iqManager.sendIQRequest("bind-resource", iq, new IQCallback() {
			@Override
			public void onIQSuccess(final IQ iq) {
				setStatus(SessionStatus.binded);
				requestSession(XmppURI.uri(iq.getExtension("bind", XmppNamespaces.BIND).getChildText("jid")));
			}

			@Override
			public void onIQFailure(final IQ iq) {
				connection.disconnect();
			}
		}, true);
	}

	// TODO: not in RFC 6121, but required by some servers
	private void requestSession(final XmppURI uri) {
		final IQ iq = new IQ(IQ.Type.set);
		iq.setFrom(uri);
		iq.setTo(uri.getHostURI());
		iq.addExtension("session", XmppNamespaces.SESSION);

		iqManager.sendIQRequest("session-request", iq, new IQCallback() {
			@Override
			public void onIQSuccess(final IQ iq) {
				userUri = iq.getTo();
				setStatus(SessionStatus.loggedIn);
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
