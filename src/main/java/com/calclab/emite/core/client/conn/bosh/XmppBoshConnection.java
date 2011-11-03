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

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.logging.Logger;

import com.calclab.emite.core.client.conn.ConnectionSettings;
import com.calclab.emite.core.client.conn.ConnectionStatus;
import com.calclab.emite.core.client.conn.XmppConnection;
import com.calclab.emite.core.client.events.ConnectionResponseEvent;
import com.calclab.emite.core.client.events.ConnectionStatusChangedEvent;
import com.calclab.emite.core.client.events.PacketReceivedEvent;
import com.calclab.emite.core.client.events.PacketSentEvent;
import com.calclab.emite.core.client.events.StanzaSentEvent;
import com.calclab.emite.core.client.util.ConnectorCallback;
import com.calclab.emite.core.client.util.ConnectorException;
import com.calclab.emite.core.client.util.Platform;
import com.calclab.emite.core.client.util.ScheduledAction;
import com.calclab.emite.core.client.xml.HasXML;
import com.calclab.emite.core.client.xml.XMLBuilder;
import com.calclab.emite.core.client.xml.XMLPacket;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * A BOSH connection implementation.
 * 
 * @see XmppConnection
 */
@Singleton
public class XmppBoshConnection implements XmppConnection, ConnectorCallback {

	private static final Logger logger = Logger.getLogger(XmppBoshConnection.class.getName());

	private final EventBus eventBus;

	private int errors;
	private boolean active;
	private int activeConnections;
	private boolean shouldCollectResponses;

	private ConnectionSettings settings;
	private StreamSettings stream;
	private XMLPacket currentBody;

	@Inject
	protected XmppBoshConnection(@Named("emite") final EventBus eventBus) {
		this.eventBus = checkNotNull(eventBus);
	}

	@Override
	public void onResponseReceived(final int statusCode, final String content, final String originalRequest) {
		clearErrors();
		activeConnections--;
		if (!active)
			return;
		
		// TODO: check if is the same code in other than FF and make
		// tests
		if (statusCode == 404) {
			eventBus.fireEventFromSource(new ConnectionStatusChangedEvent(ConnectionStatus.error, "404 Connection Error (session removed ?!) : " + content), this);
			disconnect();
		} else if (statusCode != 200 && statusCode != 0) {
			// setActive(false);
			// fireError("Bad status: " + statusCode);
			onResponseError(originalRequest, new Exception("Bad status: " + statusCode + " " + content));
		} else {
			final XMLPacket response = XMLBuilder.fromXML(content);
			if (response != null && "body".equals(response.getTagName())) {
				clearErrors();
				eventBus.fireEventFromSource(new ConnectionResponseEvent(content), this);
				handleResponse(response);
			} else {
				onResponseError(originalRequest, new Exception("Bad response: " + statusCode + " " + content));
				// fireError("Bad response: " + content);
			}
		}
	}
	
	@Override
	public void onResponseError(final String request, final Throwable throwable) {
		if (!active)
			return;
		
		final int e = incrementErrors();
		logger.severe("Connection error #" + e + ": " + throwable.getMessage());
		if (e > RetryControl.maxRetries) {
			eventBus.fireEventFromSource(new ConnectionStatusChangedEvent(ConnectionStatus.error, "Connection error: " + throwable.toString()), this);
			disconnect();
		} else {
			final int scedTime = RetryControl.retry(e);
			eventBus.fireEventFromSource(new ConnectionStatusChangedEvent(ConnectionStatus.waitingForRetry, "The connection will try to re-connect in " + scedTime + " milliseconds.", scedTime), this);
			Platform.schedule(scedTime, new ScheduledAction() {
				@Override
				public void run() {
					logger.info("Error retry: " + e);
					send(request);
				}
			});
		}
	}
	
	@Override
	public HandlerRegistration addConnectionResponseHandler(final ConnectionResponseEvent.Handler handler) {
		return eventBus.addHandlerToSource(ConnectionResponseEvent.TYPE, this, handler);
	}

	@Override
	public HandlerRegistration addConnectionStatusChangedHandler(final ConnectionStatusChangedEvent.Handler handler) {
		return eventBus.addHandlerToSource(ConnectionStatusChangedEvent.TYPE, this, handler);
	}

	@Override
	public HandlerRegistration addStanzaReceivedHandler(final PacketReceivedEvent.Handler handler) {
		return eventBus.addHandlerToSource(PacketReceivedEvent.TYPE, this, handler);
	}

	@Override
	public HandlerRegistration addStanzaSentHandler(final StanzaSentEvent.Handler handler) {
		return eventBus.addHandlerToSource(StanzaSentEvent.TYPE, this, handler);
	}

	@Override
	public void connect() {
		checkState(settings != null, "You should set user settings before connect!");
		clearErrors();

		if (!active) {
			active = true;
			activeConnections = 0;
			stream = new StreamSettings();
			createInitialBody();
			sendBody(false);
		}
	}

	@Override
	public void disconnect() {
		logger.finer("BoshConnection - Disconnected called - Clearing current body and send a priority 'terminate' stanza.");
		// Clearing all queued stanzas
		currentBody = null;
		// Create a new terminate stanza and force the send
		createBodyIfNeeded();
		currentBody.setAttribute("type", "terminate");
		sendBody(true);
		active = false;
		stream.sid = null;
		eventBus.fireEventFromSource(new ConnectionStatusChangedEvent(ConnectionStatus.disconnected, "logged out"), this);
	}

	@Override
	public boolean isConnected() {
		return stream != null;
	}

	@Override
	public StreamSettings pause() {
		if (stream != null && stream.sid != null) {
			createBodyIfNeeded();
			currentBody.setAttribute("pause", stream.getMaxPauseString());
			sendBody(true);
			return stream;
		}
		return null;
	}

	@Override
	public void restartStream() {
		createBodyIfNeeded();
		currentBody.setAttribute("xmlns:xmpp", "urn:xmpp:xbosh");
		currentBody.setAttribute("xmpp:restart", "true");
		currentBody.setAttribute("to", settings.hostName);
		currentBody.setAttribute("xml:lang", "en");
	}

	@Override
	public boolean resume(final StreamSettings settings) {
		active = true;
		stream = settings;
		continueConnection(null);
		return active;
	}

	@Override
	public void send(final HasXML packet) {
		createBodyIfNeeded();
		currentBody.addChild(packet);
		sendBody(false);
		eventBus.fireEventFromSource(new PacketSentEvent(packet.getXML()), this);
	}
	
	private void clearErrors() {
		errors = 0;
	}

	@Override
	public boolean hasErrors() {
		return errors != 0;
	}

	private int incrementErrors() {
		errors++;
		return errors;
	}

	/**
	 * @return the stream settings
	 */
	@Override
	public StreamSettings getStreamSettings() {
		return stream;
	}
	
	@Override
	public void setSettings(final ConnectionSettings settings) {
		logger.finer("Setting connection settings.");
		this.settings = settings;
	}

	@Override
	public String toString() {
		return "Bosh in " + (active ? "active" : "inactive") + " stream=" + stream;
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
			if (currentBody != null) {
				sendBody(false);
			} else {
				final long currentRID = stream.rid;
				Platform.schedule(300, new ScheduledAction() {
					@Override
					public void run() {
						if (currentBody == null && stream.rid == currentRID) {
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
		if (currentBody != null)
			return;
		
		currentBody = XMLBuilder.create("body", "http://jabber.org/protocol/httpbind").getXML();
		currentBody.setAttribute("rid", stream.getNextRid());
		if (stream != null) {
			currentBody.setAttribute("sid", stream.sid);
		}
	}

	private void createInitialBody() {
		currentBody = XMLBuilder.create("body", "http://jabber.org/protocol/httpbind").getXML();
		currentBody.setAttribute("content", "text/xml; charset=utf-8");
		currentBody.setAttribute("xml:lang", "en");
		currentBody.setAttribute("xmlns:xmpp", "urn:xmpp:xbosh");
		currentBody.setAttribute("xmpp:version", "1.0");
		currentBody.setAttribute("ver", "1.6");
		currentBody.setAttribute("ack", "1");
		currentBody.setAttribute("secure", String.valueOf(settings.secure));
		currentBody.setAttribute("rid", stream.getNextRid());
		currentBody.setAttribute("to", settings.hostName);
		if (settings.routeHost != null) {
			currentBody.setAttribute("route", "xmpp:" + settings.routeHost + ":" + settings.routePort);
		}
		currentBody.setAttribute("hold", String.valueOf(settings.hold));
		currentBody.setAttribute("wait", String.valueOf(settings.wait));
	}

	private void handleResponse(final XMLPacket response) {
		final String type = response.getAttribute("type");
		// Openfire bug: terminal instead of terminate
		if ("terminate".equals(type) || "terminal".equals(type)) {
			stream.sid = null;
			active = false;
			eventBus.fireEventFromSource(new ConnectionStatusChangedEvent(ConnectionStatus.disconnected, "disconnected by server"), this);
		} else {
			if (stream.sid == null) {
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
			Platform.send(settings.httpBase, request, this);
			stream.lastRequestTime = System.currentTimeMillis();
		} catch (final ConnectorException e) {
			activeConnections--;
			e.printStackTrace();
		}
	}

	private void sendBody(final boolean force) {
		// TODO: better semantics
		if (force || !shouldCollectResponses && active && activeConnections < ConnectionSettings.MAX_REQUESTS && !hasErrors()) {
			final String request = currentBody.toString();
			currentBody = null;
			send(request);
		} else {
			logger.finer("Send body simply queued");
		}
	}

}
