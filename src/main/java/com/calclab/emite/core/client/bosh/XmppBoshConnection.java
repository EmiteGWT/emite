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

package com.calclab.emite.core.client.bosh;

import java.util.List;
import java.util.logging.Logger;

import com.calclab.emite.core.client.conn.ConnectionSettings;
import com.calclab.emite.core.client.conn.StanzaSentEvent;
import com.calclab.emite.core.client.conn.XmppConnection;
import com.calclab.emite.core.client.conn.XmppConnectionBoilerPlate;
import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.Packet;
import com.calclab.emite.core.client.services.ConnectorCallback;
import com.calclab.emite.core.client.services.ConnectorException;
import com.calclab.emite.core.client.services.ScheduledAction;
import com.calclab.emite.core.client.services.Services;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * A Bosh connection implementation.
 * 
 * @see XmppConnection
 */
@Singleton
public class XmppBoshConnection extends XmppConnectionBoilerPlate {
	
	private static final Logger logger = Logger.getLogger(XmppBoshConnection.class.getName());
	
	private int activeConnections;
	private final Services services;
	private final ConnectorCallback listener;
	private boolean shouldCollectResponses;
	private final RetryControl retryControl = new RetryControl();

	@Inject
	public XmppBoshConnection(final EmiteEventBus eventBus, final Services services) {
		super(eventBus);
		this.services = services;

		listener = new ConnectorCallback() {

			@Override
			public void onError(final String request, final Throwable throwable) {
				if (isActive()) {
					final int e = incrementErrors();
					logger.severe("Connection error #" + e + ": " + throwable.getMessage());
					if (e > retryControl.maxRetries) {
						fireError("Connection error: " + throwable.toString());
						disconnect();
					} else {
						final int scedTime = retryControl.retry(e);
						fireRetry(e, scedTime);
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
						fireError("404 Connection Error (session removed ?!) : " + content);
						disconnect();
					} else if (statusCode != 200 && statusCode != 0) {
						// setActive(false);
						// fireError("Bad status: " + statusCode);
						onError(originalRequest, new Exception("Bad status: " + statusCode + " " + content));
					} else {
						final IPacket response = services.toXML(content);
						if (response != null && "body".equals(response.getName())) {
							clearErrors();
							fireResponse(content);
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
			sendBody();
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
		fireDisconnected("logged out");
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
	public void send(final IPacket packet) {
		createBodyIfNeeded();
		getCurrentBody().addChild(packet);
		sendBody();
		eventBus.fireEvent(new StanzaSentEvent(packet));
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
				sendBody();
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
							sendBody();
						}
					}
				});
			}
		}
	}

	private void createBodyIfNeeded() {
		if (getCurrentBody() == null) {
			final Packet body = new Packet("body");
			body.With("xmlns", "http://jabber.org/protocol/httpbind");
			body.With("rid", getStreamSettings().getNextRid());
			if (getStreamSettings() != null) {
				body.With("sid", getStreamSettings().sid);
			}
			setCurrentBody(body);
		}
	}

	private void createInitialBody(final ConnectionSettings userSettings) {
		final Packet body = new Packet("body");
		body.setAttribute("content", "text/xml; charset=utf-8");
		body.setAttribute("xmlns", "http://jabber.org/protocol/httpbind");
		body.setAttribute("xmlns:xmpp", "urn:xmpp:xbosh");
		body.setAttribute("ver", userSettings.version);
		body.setAttribute("xmpp:version", "1.0");
		body.setAttribute("xml:lang", "en");
		body.setAttribute("ack", "1");
		body.setAttribute("secure", Boolean.toString(userSettings.secure));
		body.setAttribute("rid", getStreamSettings().getNextRid());
		body.setAttribute("to", userSettings.hostName);
		if (userSettings.routeHost != null && userSettings.routePort != null) {
			String routeHost = userSettings.routeHost;
			if (routeHost == null) {
				routeHost = userSettings.hostName;
			}
			Integer routePort = userSettings.routePort;
			if (routePort == null) {
				routePort = 5222;
			}
			body.setAttribute("route", "xmpp:" + routeHost + ":" + routePort);
		}
		body.With("hold", userSettings.hold);
		body.With("wait", userSettings.wait);
		setCurrentBody(body);
	}

	private void handleResponse(final IPacket response) {
		if (isTerminate(response.getAttribute("type"))) {
			getStreamSettings().sid = null;
			setActive(false);
			fireDisconnected("disconnected by server");
		} else {
			if (getStreamSettings().sid == null) {
				initStream(response);
				fireConnected();
			}
			shouldCollectResponses = true;
			final List<? extends IPacket> stanzas = response.getChildren();
			for (final IPacket stanza : stanzas) {
				fireStanzaReceived(stanza);
			}
			shouldCollectResponses = false;
			continueConnection(response.getAttribute("ack"));
		}
	}

	private void initStream(final IPacket response) {
		final StreamSettings stream = getStreamSettings();
		stream.sid = response.getAttribute("sid");
		stream.wait = response.getAttribute("wait");
		stream.setInactivity(response.getAttribute("inactivity"));
		stream.setMaxPause(response.getAttribute("maxpause"));
	}

	private boolean isTerminate(final String type) {
		// Openfire bug: terminal instead of terminate
		return "terminate".equals(type) || "terminal".equals(type);
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
			getStreamSettings().lastRequestTime = services.getCurrentTime();
		} catch (final ConnectorException e) {
			activeConnections--;
			e.printStackTrace();
		}
	}

	private void sendBody() {
		sendBody(false);
	}

	private void sendBody(final boolean force) {
		// TODO: better semantics
		if (force || !shouldCollectResponses && isActive() && activeConnections < getConnectionSettings().maxRequests && !hasErrors()) {
			final String request = services.toString(getCurrentBody());
			setCurrentBody(null);
			send(request);
		} else {
			logger.finer("Send body simply queued");
		}
	}

}
