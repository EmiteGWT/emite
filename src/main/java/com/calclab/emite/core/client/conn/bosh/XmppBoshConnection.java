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

package com.calclab.emite.core.client.conn.bosh;

import java.util.logging.Logger;

import com.calclab.emite.core.client.conn.ConnectionSettings;
import com.calclab.emite.core.client.conn.ConnectionStatus;
import com.calclab.emite.core.client.conn.XmppConnection;
import com.calclab.emite.core.client.conn.XmppConnectionBoilerplate;
import com.calclab.emite.core.client.events.ConnectionResponseEvent;
import com.calclab.emite.core.client.events.ConnectionStatusChangedEvent;
import com.calclab.emite.core.client.events.PacketReceivedEvent;
import com.calclab.emite.core.client.events.PacketSentEvent;
import com.calclab.emite.core.client.services.ConnectorCallback;
import com.calclab.emite.core.client.services.ConnectorException;
import com.calclab.emite.core.client.services.ScheduledAction;
import com.calclab.emite.core.client.services.Services;
import com.calclab.emite.core.client.xml.HasXML;
import com.calclab.emite.core.client.xml.XMLBuilder;
import com.calclab.emite.core.client.xml.XMLPacket;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.web.bindery.event.shared.EventBus;

/**
 * A Bosh connection implementation.
 * 
 * @see XmppConnection
 */
@Singleton
public class XmppBoshConnection extends XmppConnectionBoilerplate {

	private static final Logger logger = Logger.getLogger(XmppBoshConnection.class.getName());

	private final Services services;
	private int activeConnections;
	private final ConnectorCallback listener;
	private boolean shouldCollectResponses;

	@Inject
	public XmppBoshConnection(@Named("emite") final EventBus eventBus, final Services services) {
		super(eventBus);

		this.services = services;

		listener = new ConnectorCallback() {

			@Override
			public void onError(final String request, final Throwable throwable) {
				if (isActive()) {
					final int e = incrementErrors();
					logger.severe("Connection error #" + e + ": " + throwable.getMessage());
					if (e > RetryControl.maxRetries) {
						eventBus.fireEventFromSource(new ConnectionStatusChangedEvent(ConnectionStatus.error, "Connection error: " + throwable.toString()), this);
						disconnect();
					} else {
						final int scedTime = RetryControl.retry(e);
						eventBus.fireEventFromSource(new ConnectionStatusChangedEvent(ConnectionStatus.waitingForRetry, "The connection will try to re-connect in " + scedTime + " milliseconds.", scedTime), this);
						services.schedule(scedTime, new ScheduledAction() {
							@Override
							public void run() {
								logger.info("Error retry: " + e);
								send(request);
							}
						});
					}
				}
			}

			@Override
			public void onResponseReceived(final int statusCode, final String content, final String originalRequest) {
				clearErrors();
				activeConnections--;
				if (isActive()) {
					// TODO: check if is the same code in other than FF and make
					// tests
					if (statusCode == 404) {
						eventBus.fireEventFromSource(new ConnectionStatusChangedEvent(ConnectionStatus.error, "404 Connection Error (session removed ?!) : " + content), this);
						disconnect();
					} else if (statusCode != 200 && statusCode != 0) {
						// setActive(false);
						// fireError("Bad status: " + statusCode);
						onError(originalRequest, new Exception("Bad status: " + statusCode + " " + content));
					} else {
						final XMLPacket response = XMLBuilder.fromXML(content);
						if (response != null && "body".equals(response.getTagName())) {
							clearErrors();
							eventBus.fireEventFromSource(new ConnectionResponseEvent(content), this);
							handleResponse(response);
						} else {
							onError(originalRequest, new Exception("Bad response: " + statusCode + " " + content));
							// fireError("Bad response: " + content);
						}
					}
				}
			}
		};
	}

	@Override
	public void connect() {
		assert getConnectionSettings() != null : "You should set user settings before connect!";
		clearErrors();

		if (!isActive()) {
			setActive(true);
			setStream(new StreamSettings());
			activeConnections = 0;
			createInitialBody(getConnectionSettings());
			sendBody(false);
		}
	}

	@Override
	public void disconnect() {
		logger.finer("BoshConnection - Disconnected called - Clearing current body and send a priority 'terminate' stanza.");
		// Clearing all queued stanzas
		setCurrentBody(null);
		// Create a new terminate stanza and force the send
		createBodyIfNeeded();
		getCurrentBody().setAttribute("type", "terminate");
		sendBody(true);
		setActive(false);
		getStreamSettings().sid = null;
		eventBus.fireEventFromSource(new ConnectionStatusChangedEvent(ConnectionStatus.disconnected, "logged out"), this);
	}

	@Override
	public boolean isConnected() {
		return getStreamSettings() != null;
	}

	@Override
	public StreamSettings pause() {
		if (getStreamSettings() != null && getStreamSettings().sid != null) {
			createBodyIfNeeded();
			getCurrentBody().setAttribute("pause", getStreamSettings().getMaxPauseString());
			sendBody(true);
			return getStreamSettings();
		}
		return null;
	}

	@Override
	public void restartStream() {
		createBodyIfNeeded();
		getCurrentBody().setAttribute("xmlns:xmpp", "urn:xmpp:xbosh");
		getCurrentBody().setAttribute("xmpp:restart", "true");
		getCurrentBody().setAttribute("to", getConnectionSettings().hostName);
		getCurrentBody().setAttribute("xml:lang", "en");
	}

	@Override
	public boolean resume(final StreamSettings settings) {
		setActive(true);
		setStream(settings);
		continueConnection(null);
		return isActive();
	}

	@Override
	public void send(final HasXML packet) {
		createBodyIfNeeded();
		getCurrentBody().addChild(packet);
		sendBody(false);
		eventBus.fireEventFromSource(new PacketSentEvent(packet.getXML()), this);
	}

	@Override
	public String toString() {
		return "Bosh in " + (isActive() ? "active" : "inactive") + " stream=" + getStreamSettings();
	}

	/**
	 * After receiving a response from the connection manager, if none of the
	 * client's requests are still being held by the connection manager (and if
	 * the session is not a Polling Session), the client SHOULD make a new
	 * request as soon as possible. In any case, if no requests are being held,
	 * the client MUST make a new request before the maximum inactivity period
	 * has expired. The length of this period (in seconds) is specified by the
	 * 'inactivity' attribute in the session creation response.
	 * 
	 * @see http://xmpp.org/extensions/xep-0124.html#inactive
	 * @param ack
	 */
	private void continueConnection(final String ack) {
		if (isConnected() && activeConnections == 0) {
			if (getCurrentBody() != null) {
				sendBody(false);
			} else {
				final long currentRID = getStreamSettings().rid;
				final int waitTime = 300;
				services.schedule(waitTime, new ScheduledAction() {
					@Override
					public void run() {
						if (getCurrentBody() == null && getStreamSettings().rid == currentRID) {
							createBodyIfNeeded();
							// Whitespace keep-alive
							// getCurrentBody().setText(" ");
							sendBody(false);
						}
					}
				});
			}
		}
	}

	private void createBodyIfNeeded() {
		if (getCurrentBody() == null) {
			final XMLPacket body = XMLBuilder.create("body", "http://jabber.org/protocol/httpbind").getXML();
			body.setAttribute("rid", getStreamSettings().getNextRid());
			if (getStreamSettings() != null) {
				body.setAttribute("sid", getStreamSettings().sid);
			}
			setCurrentBody(body);
		}
	}

	private void createInitialBody(final ConnectionSettings userSettings) {
		final XMLPacket body = XMLBuilder.create("body", "http://jabber.org/protocol/httpbind").getXML();
		body.setAttribute("content", "text/xml; charset=utf-8");
		body.setAttribute("xml:lang", "en");
		body.setAttribute("xmlns:xmpp", "urn:xmpp:xbosh");
		body.setAttribute("xmpp:version", "1.0");
		body.setAttribute("ver", "1.6");
		body.setAttribute("ack", "1");
		body.setAttribute("secure", String.valueOf(userSettings.secure));
		body.setAttribute("rid", getStreamSettings().getNextRid());
		body.setAttribute("to", userSettings.hostName);
		if (userSettings.routeHost != null) {
			body.setAttribute("route", "xmpp:" + userSettings.routeHost + ":" + userSettings.routePort);
		}
		body.setAttribute("hold", String.valueOf(userSettings.hold));
		body.setAttribute("wait", String.valueOf(userSettings.wait));
		setCurrentBody(body);
	}

	private void handleResponse(final XMLPacket response) {
		final String type = response.getAttribute("type");
		// Openfire bug: terminal instead of terminate
		if ("terminate".equals(type) || "terminal".equals(type)) {
			getStreamSettings().sid = null;
			setActive(false);
			eventBus.fireEventFromSource(new ConnectionStatusChangedEvent(ConnectionStatus.disconnected, "disconnected by server"), this);
		} else {
			if (getStreamSettings().sid == null) {
				initStream(response);
				eventBus.fireEventFromSource(new ConnectionStatusChangedEvent(ConnectionStatus.connected), this);
			}
			shouldCollectResponses = true;
			for (final XMLPacket packet : response.getChildren()) {
				eventBus.fireEventFromSource(new PacketReceivedEvent(packet), this);
			}
			shouldCollectResponses = false;
			continueConnection(response.getAttribute("ack"));
		}
	}

	private void initStream(final XMLPacket response) {
		final StreamSettings stream = getStreamSettings();
		stream.sid = response.getAttribute("sid");
		stream.wait = response.getAttribute("wait");
		stream.setInactivity(response.getAttribute("inactivity"));
		stream.setMaxPause(response.getAttribute("maxpause"));
	}

	/**
	 * Sends a new request (and count the activeConnections)
	 * 
	 * @param request
	 */
	private void send(final String request) {
		try {
			activeConnections++;
			services.send(getConnectionSettings().httpBase, request, listener);
			getStreamSettings().lastRequestTime = System.currentTimeMillis();
		} catch (final ConnectorException e) {
			activeConnections--;
			e.printStackTrace();
		}
	}

	private void sendBody(final boolean force) {
		// TODO: better semantics
		if (force || !shouldCollectResponses && isActive() && activeConnections < ConnectionSettings.MAX_REQUESTS && !hasErrors()) {
			final String request = getCurrentBody().toString();
			setCurrentBody(null);
			send(request);
		} else {
			logger.finer("Send body simply queued");
		}
	}

}
