/*
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008-2009 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.calclab.emite.core.client.bosh;

import java.util.List;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.Packet;
import com.calclab.emite.core.client.services.ConnectorCallback;
import com.calclab.emite.core.client.services.ConnectorException;
import com.calclab.emite.core.client.services.ScheduledAction;
import com.calclab.emite.core.client.services.Services;
import com.calclab.suco.client.events.Event;
import com.calclab.suco.client.events.Event0;
import com.calclab.suco.client.events.Listener;
import com.google.gwt.core.client.GWT;

public class BoshConnection implements Connection {
    private int activeConnections;
    private Packet body;
    private final Services services;
    private final ConnectorCallback listener;
    private boolean running;
    private StreamSettings stream;
    private final Event<String> onError;
    private final Event<String> onDisconnected;
    private final Event0 onConnected;
    private final Event<IPacket> onStanzaReceived;
    private final Event<IPacket> onStanzaSent;
    private boolean shouldCollectResponses;
    private BoshSettings userSettings;
    private int errors;
    private final Event<String> onResponse;

    public BoshConnection(final Services services) {
	this.services = services;
	this.onError = new Event<String>("bosh:onError");
	this.onDisconnected = new Event<String>("bosh:onDisconnected");
	this.onConnected = new Event0("bosh:onConnected");
	this.onStanzaReceived = new Event<IPacket>("bosh:onReceived");
	this.onResponse = new Event<String>("bosh:onResponse");
	this.onStanzaSent = new Event<IPacket>("bosh:onSent");
	this.errors = 0;

	this.listener = new ConnectorCallback() {

	    public void onError(final String request, final Throwable throwable) {
		if (running) {
		    GWT.log("Connection error (total: " + errors + ")", throwable);
		    errors++;
		    if (errors > 2) {
			running = false;
			onError.fire("Connection error: " + throwable.toString());
		    } else {
			GWT.log("Error retry: " + throwable, null);
			send(request);
		    }
		}
	    }

	    public void onResponseReceived(final int statusCode, final String content) {
		errors = 0;
		activeConnections--;
		if (running) {
		    // TODO: check if is the same code in other than FF and make
		    // tests
		    if (statusCode != 200 && statusCode != 0) {
			running = false;
			onError.fire("Bad status: " + statusCode);
		    } else {
			onResponse.fire(content);
			final IPacket response = services.toXML(content);
			if (response != null && "body".equals(response.getName())) {
			    handleResponse(response);
			} else {
			    onError.fire("Bad response: " + content);
			}
		    }
		}

	    }
	};
    }

    public void connect() {
	assert userSettings != null;

	if (!running) {
	    this.running = true;
	    this.stream = new StreamSettings();
	    this.activeConnections = 0;
	    createInitialBody(userSettings);
	    sendBody();
	}
    }

    public void disconnect() {
	GWT.log("BoshConnection - Disconnected called.", null);
	createBody();
	body.setAttribute("type", "terminate");
	sendBody();
	running = false;
	stream.sid = null;
	onDisconnected.fire("logged out");
    }

    public boolean isConnected() {
	return stream != null;
    }

    public void onError(final Listener<String> listener) {
	onError.add(listener);
    }

    public void onResponse(final Listener<String> listener) {
	onResponse.add(listener);
    }

    public void onStanzaReceived(final Listener<IPacket> listener) {
	onStanzaReceived.add(listener);
    }

    public void onStanzaSent(final Listener<IPacket> listener) {
	onStanzaSent.add(listener);
    }

    public StreamSettings pause() {
	if (stream != null && stream.sid != null) {
	    createBody();
	    body.setAttribute("pause", stream.maxPause);
	    sendBody();
	    return stream;
	}
	return null;
    }

    public void removeOnStanzaReceived(final Listener<IPacket> listener) {
	onStanzaReceived.remove(listener);
    }

    public void restartStream() {
	createBody();
	body.setAttribute("xmlns:xmpp", "urn:xmpp:xbosh");
	body.setAttribute("xmpp:restart", "true");
	body.setAttribute("to", userSettings.hostName);
	body.setAttribute("xml:lang", "en");
    }

    public boolean resume(final StreamSettings settings) {
	running = true;
	stream = settings;
	continueConnection(null);
	return running;
    }

    public void send(final IPacket packet) {
	createBody();
	body.addChild(packet);
	sendBody();
	onStanzaSent.fire(packet);
    }

    public void setSettings(final BoshSettings settings) {
	this.userSettings = settings;
    }

    private void continueConnection(final String ack) {
	if (isConnected() && activeConnections == 0) {
	    if (body != null) {
		sendBody();
	    } else {
		final long currentRID = stream.rid;
		// FIXME: hardcoded
		final int msecs = 6000;
		services.schedule(msecs, new ScheduledAction() {
		    public void run() {
			if (body == null && stream.rid == currentRID) {
			    createBody();
			    sendBody();
			}
		    }
		});
	    }
	}
    }

    private void createBody() {
	if (body == null) {
	    this.body = new Packet("body");
	    body.With("xmlns", "http://jabber.org/protocol/httpbind");
	    body.With("rid", stream.getNextRid());
	    if (stream != null) {
		body.With("sid", stream.sid);
	    }
	}
    }

    private void createInitialBody(final BoshSettings userSettings) {
	this.body = new Packet("body");
	body.setAttribute("content", "text/xml; charset=utf-8");
	body.setAttribute("xmlns", "http://jabber.org/protocol/httpbind");
	body.setAttribute("xmlns:xmpp", "urn:xmpp:xbosh");
	body.setAttribute("ver", userSettings.version);
	body.setAttribute("xmpp:version", "1.0");
	body.setAttribute("xml:lang", "en");
	body.setAttribute("ack", "1");
	body.setAttribute("secure", "true");
	body.setAttribute("rid", stream.getNextRid());
	body.setAttribute("to", userSettings.hostName);
	body.With("hold", userSettings.hold);
	body.With("wait", userSettings.wait);
    }

    private void handleResponse(final IPacket response) {
	if (isTerminate(response.getAttribute("type"))) {
	    stream.sid = null;
	    onDisconnected.fire("disconnected by server");
	} else {
	    if (stream.sid == null) {
		initStream(response);
		onConnected.fire();
	    }
	    shouldCollectResponses = true;
	    final List<? extends IPacket> stanzas = response.getChildren();
	    for (final IPacket stanza : stanzas) {
		onStanzaReceived.fire(stanza);
	    }
	    shouldCollectResponses = false;
	    continueConnection(response.getAttribute("ack"));
	}
    }

    private void initStream(final IPacket response) {
	stream.sid = response.getAttribute("sid");
	stream.wait = response.getAttribute("wait");
	stream.inactivity = response.getAttribute("inactivity");
	stream.maxPause = response.getAttribute("maxpause");
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
	    services.send(userSettings.httpBase, request, listener);
	    stream.lastRequestTime = services.getCurrentTime();
	} catch (final ConnectorException e) {
	    activeConnections--;
	    e.printStackTrace();
	}
    }

    private void sendBody() {
	if (!shouldCollectResponses) {
	    final String request = services.toString(body);
	    body = null;
	    send(request);
	}
    }
}
