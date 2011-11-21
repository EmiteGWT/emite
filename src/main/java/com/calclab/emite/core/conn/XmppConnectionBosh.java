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

package com.calclab.emite.core.conn;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Nullable;

import com.calclab.emite.base.util.Platform;
import com.calclab.emite.base.util.ScheduledAction;
import com.calclab.emite.base.xml.HasXML;
import com.calclab.emite.base.xml.XMLBuilder;
import com.calclab.emite.base.xml.XMLPacket;
import com.calclab.emite.core.AsyncResult;
import com.calclab.emite.core.XmppNamespaces;
import com.calclab.emite.core.events.ConnectionStatusChangedEvent;
import com.calclab.emite.core.events.PacketReceivedEvent;
import com.calclab.emite.core.events.PacketSentEvent;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * A BOSH XMPP connection implementation.
 * 
 * @see XmppConnection
 */
@Singleton
public final class XmppConnectionBosh implements XmppConnection {

	private static final Logger logger = Logger.getLogger(XmppConnectionBosh.class.getName());

	private final EventBus eventBus;
	private final List<XMLPacket> currentRequests;
	
	@Nullable private ConnectionSettings settings;
	@Nullable private StreamSettings stream;
	@Nullable private XMLPacket currentBody;

	private int errors;
	private boolean active;
	private boolean shouldCollectResponses;

	@Inject
	protected XmppConnectionBosh(@Named("emite") final EventBus eventBus) {
		this.eventBus = checkNotNull(eventBus);
		currentRequests = Lists.newArrayList();
	}

	@Override
	public HandlerRegistration addConnectionStatusChangedHandler(final ConnectionStatusChangedEvent.Handler handler) {
		return eventBus.addHandlerToSource(ConnectionStatusChangedEvent.TYPE, this, handler);
	}

	@Override
	public HandlerRegistration addPacketReceivedHandler(final PacketReceivedEvent.Handler handler) {
		return eventBus.addHandlerToSource(PacketReceivedEvent.TYPE, this, handler);
	}

	@Override
	public HandlerRegistration addPacketSentHandler(final PacketSentEvent.Handler handler) {
		return eventBus.addHandlerToSource(PacketSentEvent.TYPE, this, handler);
	}

	@Override
	public void connect() {
		checkState(settings != null, "You must set connection settings before connecting!");

		if (!active) {
			active = true;
			errors = 0;
			currentRequests.clear();
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
		currentBody.setAttribute("xmlns:xmpp", XmppNamespaces.BIND);
		currentBody.setAttribute("xmpp:restart", "true");
		currentBody.setAttribute("to", settings.getHostName());
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
	
	@Override
	public boolean hasErrors() {
		return errors != 0;
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

	private void continueConnection(final String ack) {
		if (isConnected() && currentRequests.isEmpty()) {
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
		
		currentBody = XMLBuilder.create("body", XmppNamespaces.HTTPBIND).getXML();
		currentBody.setAttribute("rid", stream.getNextRid());
		if (stream != null) {
			currentBody.setAttribute("sid", stream.sid);
		}
	}

	private void createInitialBody() {
		currentBody = XMLBuilder.create("body", XmppNamespaces.HTTPBIND).getXML();
		currentBody.setAttribute("content", "text/xml; charset=utf-8");
		currentBody.setAttribute("xml:lang", "en");
		currentBody.setAttribute("xmlns:xmpp", XmppNamespaces.BIND);
		currentBody.setAttribute("xmpp:version", "1.0");
		currentBody.setAttribute("ver", "1.6");
		currentBody.setAttribute("ack", "1");
		currentBody.setAttribute("secure", String.valueOf(settings.isSecure()));
		currentBody.setAttribute("rid", stream.getNextRid());
		currentBody.setAttribute("to", settings.getHostName());
		if (settings.getRouteHost() != null) {
			currentBody.setAttribute("route", "xmpp:" + settings.getRouteHost() + ":" + settings.getRoutePort());
		}
		currentBody.setAttribute("hold", String.valueOf(settings.getHold()));
		currentBody.setAttribute("wait", String.valueOf(settings.getWait()));
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
	 * Sends a new request (and count the activeConnections).
	 * 
	 * @param request the request contents
	 */
	private void send(final XMLPacket request) {
		currentRequests.add(request);
		Platform.sendXML(settings.getHttpBase(), request, new AsyncResult<XMLPacket>() {
			@Override
			public void onSuccess(final XMLPacket result) {
				if (!active)
					return;
				
				errors = 0;
				currentRequests.remove(request);
				handleResponse(result);
			}

			@Override
			public void onError(final Throwable error) {
				if (!active)
					return;
				
				final int e = ++errors;
				logger.severe("Connection error #" + e + ": " + error.getMessage());
				if (e > RetryControl.maxRetries) {
					eventBus.fireEventFromSource(new ConnectionStatusChangedEvent(ConnectionStatus.error, "Connection error: " + error.toString()), this);
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
		});
		stream.lastRequestTime = System.currentTimeMillis();
	}

	private void sendBody(final boolean force) {
		// TODO: better semantics
		if (force || !shouldCollectResponses && active && currentRequests.size() < ConnectionSettings.MAX_REQUESTS && !hasErrors()) {
			send(currentBody);
			currentBody = null;
		} else {
			logger.finer("Send body simply queued");
		}
	}

	private static class RetryControl {
		public static int maxRetries = 8;

		public static final int retry(final int nbErrors) {
			return 500 + (nbErrors - 1) * nbErrors * 550;
		}
	}
	
}
