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
package com.calclab.emite.core.client.xmpp.session;

import java.util.ArrayList;

import com.calclab.emite.core.client.bosh.StreamSettings;
import com.calclab.emite.core.client.conn.ConnectionStateEvent;
import com.calclab.emite.core.client.conn.ConnectionStateEvent.ConnectionState;
import com.calclab.emite.core.client.conn.ConnectionStateHandler;
import com.calclab.emite.core.client.conn.StanzaEvent;
import com.calclab.emite.core.client.conn.StanzaHandler;
import com.calclab.emite.core.client.conn.XmppConnection;
import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.xmpp.resource.ResourceBindResultEvent;
import com.calclab.emite.core.client.xmpp.resource.ResourceBindResultHandler;
import com.calclab.emite.core.client.xmpp.resource.ResourceBindingManager;
import com.calclab.emite.core.client.xmpp.sasl.AuthorizationResultEvent;
import com.calclab.emite.core.client.xmpp.sasl.AuthorizationResultHandler;
import com.calclab.emite.core.client.xmpp.sasl.SASLManager;
import com.calclab.emite.core.client.xmpp.stanzas.IQ;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.Presence;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.google.gwt.core.client.GWT;

/**
 * Default XmppSession logic implementation. You should use XmppSession
 * interface instead.
 * 
 * @see XmppSession
 */
public class XmppSessionLogic extends XmppSessionBoilerPlate {
    private XmppURI userURI;
    private final XmppConnection connection;
    private final IQManager iqManager;
    private final ArrayList<IPacket> queuedStanzas;
    private Credentials credentials;

    public XmppSessionLogic(final XmppConnection connection, final SASLManager saslManager,
	    final ResourceBindingManager bindingManager, final IMSessionManager iMSessionManager) {
	super(connection.getEventBus());
	this.connection = connection;
	iqManager = new IQManager();
	queuedStanzas = new ArrayList<IPacket>();

	connection.addStanzaReceivedHandler(new StanzaHandler() {
	    @Override
	    public void onStanza(final StanzaEvent event) {
		final IPacket stanza = event.getStanza();
		final String name = stanza.getName();
		if (name.equals("message")) {
		    fireMessage(new Message(stanza));
		} else if (name.equals("presence")) {
		    firePresence(new Presence(stanza));
		} else if (name.equals("iq")) {
		    final String type = stanza.getAttribute("type");
		    if ("get".equals(type) || "set".equals(type)) {
			fireIQ(new IQ(stanza));
		    } else {
			iqManager.handle(stanza);
		    }
		} else if (credentials != null && "stream:features".equals(name) && stanza.hasChild("mechanisms")) {
		    setSessionState(SessionStates.connecting);
		    saslManager.sendAuthorizationRequest(credentials);
		    credentials = null;
		}
	    }
	});

	connection.addConnectionHandler(new ConnectionStateHandler() {
	    @Override
	    public void onStateChanged(final ConnectionStateEvent event) {
		if (event.is(ConnectionState.error)) {
		    GWT.log("Connection error: " + event.getDescription());
		    setSessionState(SessionStates.error);
		} else if (event.is(ConnectionState.disconnected)) {
		    setSessionState(SessionStates.disconnected);
		}
	    }
	});

	// Do not use manager, in order to be able to mock on testing
	AuthorizationResultEvent.bind(eventBus, new AuthorizationResultHandler() {
	    @Override
	    public void onAuthorization(final AuthorizationResultEvent event) {
		if (event.isSucceed()) {
		    setSessionState(SessionStates.authorized);
		    connection.restartStream();
		    bindingManager.bindResource(event.getXmppUri().getResource());
		} else {
		    setSessionState(SessionStates.notAuthorized);
		    disconnect();
		}
	    }
	});

	// Do not use manager, in order to be able to mock on testing
	ResourceBindResultEvent.bind(eventBus, new ResourceBindResultHandler() {
	    @Override
	    public void onBinded(final ResourceBindResultEvent event) {
		setSessionState(SessionStates.binded);
		iMSessionManager.requestSession(event.getXmppUri());
	    }
	});

	// Do not use manager, in order to be able to mock on testing
	SessionRequestResultEvent.bind(eventBus, new SessionRequestResultHandler() {
	    @Override
	    public void onSessionRequestResult(final SessionRequestResultEvent event) {
		if (event.isSucceed()) {
		    setLoggedIn(event.getXmppUri());
		} else {
		    disconnect();
		}
	    }
	});
    }

    private void disconnect() {
	connection.disconnect();
    }

    @Override
    public XmppURI getCurrentUser() {
	return userURI;
    }

    @Override
    public boolean isLoggedIn() {
	return userURI != null;
    }

    @Override
    public boolean isReady() {
	return userURI != null;
    }

    @Override
    public void login(final Credentials credentials) {
	if (getSessionState() == XmppSession.SessionStates.disconnected) {
	    setSessionState(XmppSession.SessionStates.connecting);
	    connection.connect();
	    this.credentials = credentials;
	}

    }

    @Override
    public void logout() {
	if (getSessionState() != XmppSession.SessionStates.disconnected && userURI != null) {
	    // TODO : To be reviewed, preventing unvailable presences to be sent
	    // so that only the 'terminate' is sent
	    // Unvailabel are handled automatically by the server
	    setSessionState(SessionStates.loggingOut);
	    userURI = null;
	    connection.disconnect();
	    setSessionState(XmppSession.SessionStates.disconnected);
	}
    }

    @Override
    public StreamSettings pause() {
	return connection.pause();
    }

    @Override
    public void resume(final XmppURI userURI, final StreamSettings settings) {
	this.userURI = userURI;
	setSessionState(XmppSession.SessionStates.resume);
	connection.resume(settings);
	setSessionState(XmppSession.SessionStates.ready);
    }

    @Override
    public void send(final IPacket packet) {
	// Added a condition to check the connection is not retrying...
	if (!connection.hasErrors()
		&& (getSessionState() == XmppSession.SessionStates.loggedIn
			|| getSessionState() == XmppSession.SessionStates.ready || getSessionState() == XmppSession.SessionStates.loggingOut)) {
	    packet.setAttribute("from", userURI.toString());
	    connection.send(packet);
	} else {
	    GWT.log("session queuing stanza" + packet, null);
	    queuedStanzas.add(packet);
	}
    }

    @Override
    public void sendIQ(final String category, final IQ iq, final IQResponseHandler handler) {
	final String id = iqManager.register(category, handler);
	iq.setAttribute("id", id);
	send(iq);
    }

    private void sendQueuedStanzas() {
	GWT.log("Sending queued stanzas....", null);
	for (final IPacket packet : queuedStanzas) {
	    send(packet);
	}
	queuedStanzas.clear();
    }

    private void setLoggedIn(final XmppURI userURI) {
	this.userURI = userURI;
	GWT.log("SESSION LOGGED IN");
	setSessionState(XmppSession.SessionStates.loggedIn);
    }

    @Override
    public void setReady() {
	if (isLoggedIn()) {
	    setSessionState(XmppSession.SessionStates.ready);
	}
    }

    @Override
    protected void setSessionState(final String newState) {
	if (newState == XmppSession.SessionStates.ready) {
	    sendQueuedStanzas();
	}
	super.setSessionState(newState);
    }

    @Override
    public String toString() {
	return "Session " + userURI + " in " + getSessionState() + " " + queuedStanzas.size() + " queued stanzas con="
		+ connection.toString();
    }
}
