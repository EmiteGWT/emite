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
import com.calclab.emite.core.client.conn.ConnectionEvent;
import com.calclab.emite.core.client.conn.ConnectionHandler;
import com.calclab.emite.core.client.conn.StanzaReceivedEvent;
import com.calclab.emite.core.client.conn.StanzaReceivedHandler;
import com.calclab.emite.core.client.conn.XmppConnection;
import com.calclab.emite.core.client.conn.ConnectionEvent.EventType;
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
 * Default Session implementation. Use Session interface instead.
 */
public class DefaultXmppSession extends AbstractXmppSession {
    private XmppURI userURI;
    private final XmppConnection connection;
    private final IQManager iqManager;
    private final ArrayList<IPacket> queuedStanzas;
    private Credentials credentials;

    public DefaultXmppSession(final XmppConnection connection, final SASLManager saslManager,
	    final ResourceBindingManager bindingManager, final IMSessionManager iMSessionManager) {
	super(connection.getEventBus());
	this.connection = connection;
	iqManager = new IQManager();
	queuedStanzas = new ArrayList<IPacket>();

	connection.addStanzaReceivedHandler(new StanzaReceivedHandler() {
	    @Override
	    public void onStanzaReceived(final StanzaReceivedEvent event) {
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
		    saslManager.sendAuthorizationRequest(credentials);
		    credentials = null;
		}
	    }
	});

	connection.addConnectionHandler(new ConnectionHandler() {
	    @Override
	    public void onStateChanged(final ConnectionEvent event) {
		if (event.is(EventType.error)) {
		    GWT.log("Connection error: " + event.getText());
		    setSessionState(SessionState.error);
		} else if (event.is(EventType.disconnected)) {
		    setSessionState(SessionState.disconnected);
		}
	    }
	});

	eventBus.addHandler(AuthorizationResultEvent.getType(), new AuthorizationResultHandler() {
	    @Override
	    public void onAuthorization(final AuthorizationResultEvent event) {
		if (event.isSucceed()) {
		    setSessionState(SessionState.authorized);
		    connection.restartStream();
		    bindingManager.bindResource(event.getXmppUri().getResource());
		} else {
		    setSessionState(SessionState.notAuthorized);
		    disconnect();
		}
	    }
	});

	eventBus.addHandler(ResourceBindResultEvent.getType(), new ResourceBindResultHandler() {
	    @Override
	    public void onBinded(final ResourceBindResultEvent event) {
		iMSessionManager.requestSession(event.getXmppUri());
	    }
	});

	eventBus.addHandler(SessionRequestResultEvent.getType(), new SessionRequestResultHandler() {
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

    public XmppURI getCurrentUser() {
	return userURI;
    }

    public boolean isLoggedIn() {
	return userURI != null;
    }

    @Override
    public void login(final Credentials credentials) {
	if (getSessionState() == XmppSession.SessionState.disconnected) {
	    setSessionState(XmppSession.SessionState.connecting);
	    connection.connect();
	    this.credentials = credentials;
	}

    }

    public void logout() {
	if (getSessionState() != XmppSession.SessionState.disconnected && userURI != null) {
	    // TODO : To be reviewed, preventing unvailable presences to be sent
	    // so that only the 'terminate' is sent
	    // Unvailabel are handled automatically by the server
	    // setState(State.loggingOut);
	    userURI = null;
	    connection.disconnect();
	    setSessionState(XmppSession.SessionState.disconnected);
	}
    }

    public StreamSettings pause() {
	return connection.pause();
    }

    public void resume(final XmppURI userURI, final StreamSettings settings) {
	this.userURI = userURI;
	setSessionState(XmppSession.SessionState.resume);
	connection.resume(settings);
	setSessionState(XmppSession.SessionState.ready);
    }

    public void send(final IPacket packet) {
	// Added a condition to check the connection is not retrying...
	if (!connection.hasErrors()
		&& (getSessionState() == XmppSession.SessionState.loggedIn
			|| getSessionState() == XmppSession.SessionState.ready || getSessionState() == XmppSession.SessionState.loggingOut)) {
	    packet.setAttribute("from", userURI.toString());
	    connection.send(packet);
	} else {
	    GWT.log("session queuing stanza" + packet, null);
	    queuedStanzas.add(packet);
	}
    }

    public void sendIQ(final String category, final IQ iq, final IQResponseHandler handler) {
	final String id = iqManager.register(category, handler);
	iq.setAttribute("id", id);
	send(iq);
    }

    public void setReady() {
	if (isLoggedIn()) {
	    setSessionState(XmppSession.SessionState.ready);
	}
    }

    @Override
    public String toString() {
	return "Session " + userURI + " in " + getSessionState() + " " + queuedStanzas.size() + " queued stanzas con="
		+ connection.toString();
    }

    private void disconnect() {
	connection.disconnect();
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
	setSessionState(XmppSession.SessionState.loggedIn);
    }

    @Override
    protected void setSessionState(final String newState) {
	if (newState == XmppSession.SessionState.ready) {
	    sendQueuedStanzas();
	}
	super.setSessionState(newState);
    }
}
