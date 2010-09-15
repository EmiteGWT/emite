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
import com.calclab.emite.core.client.conn.ConnectionStateChangedEvent;
import com.calclab.emite.core.client.conn.ConnectionStateChangedEvent.ConnectionState;
import com.calclab.emite.core.client.conn.ConnectionStateChangedHandler;
import com.calclab.emite.core.client.conn.StanzaEvent;
import com.calclab.emite.core.client.conn.StanzaHandler;
import com.calclab.emite.core.client.conn.XmppConnection;
import com.calclab.emite.core.client.events.BeforeStanzaSendEvent;
import com.calclab.emite.core.client.events.IQReceivedEvent;
import com.calclab.emite.core.client.events.MessageReceivedEvent;
import com.calclab.emite.core.client.events.PresenceReceivedEvent;
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
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Default XmppSession logic implementation. You should use XmppSession
 * interface instead.
 * 
 * @see XmppSession
 */
@Singleton
public class XmppSessionLogic extends XmppSessionBoilerPlate {
    private XmppURI userUri;
    private final XmppConnection connection;
    private final IQManager iqManager;
    private final ArrayList<IPacket> queuedStanzas;
    private Credentials credentials;
    private final SessionComponentsRegistry registry;

    @Inject
    public XmppSessionLogic(final XmppConnection connection, final SASLManager saslManager,
	    final ResourceBindingManager bindingManager, final IMSessionManager iMSessionManager,
	    SessionComponentsRegistry registry) {
	super(connection.getEventBus());
	GWT.log("Creating XmppSession");
	this.registry = registry;
	this.connection = connection;
	iqManager = new IQManager();
	queuedStanzas = new ArrayList<IPacket>();

	connection.addStanzaReceivedHandler(new StanzaHandler() {
	    @Override
	    public void onStanza(final StanzaEvent event) {
		final IPacket stanza = event.getStanza();
		final String name = stanza.getName();
		if (name.equals("message")) {
		    eventBus.fireEvent(new MessageReceivedEvent(new Message(stanza)));
		} else if (name.equals("presence")) {
		    eventBus.fireEvent(new PresenceReceivedEvent(new Presence(stanza)));
		} else if (name.equals("iq")) {
		    final String type = stanza.getAttribute("type");
		    if ("get".equals(type) || "set".equals(type)) {
			eventBus.fireEvent(new IQReceivedEvent(new IQ(stanza)));
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

	connection.addConnectionStateChangedHandler(new ConnectionStateChangedHandler() {
	    @Override
	    public void onStateChanged(final ConnectionStateChangedEvent event) {
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

    @Override
    public XmppURI getCurrentUser() {
	return userUri;
    }

    @Override
    public boolean isLoggedIn() {
	return userUri != null;
    }

    @Override
    public boolean isReady() {
	return userUri != null;
    }

    @Override
    public void login(final Credentials credentials) {
	GWT.log("XmppSessionLogic - login");
	if (!registry.areComponentsCreated()) {
	    GWT.log("First login - Creating session components");
	    registry.createComponents();
	}
	if (getSessionState() == XmppSession.SessionStates.disconnected) {
	    setSessionState(XmppSession.SessionStates.connecting);
	    connection.connect();
	    this.credentials = credentials;
	}

    }

    @Override
    public void logout() {
	if (getSessionState() != XmppSession.SessionStates.disconnected && userUri != null) {
	    // TODO : To be reviewed, preventing unvailable presences to be sent
	    // so that only the 'terminate' is sent
	    // Unvailabel are handled automatically by the server
	    setSessionState(SessionStates.loggingOut);
	    userUri = null;
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
	this.userUri = userURI;
	setSessionState(XmppSession.SessionStates.resume);
	connection.resume(settings);
	setSessionState(XmppSession.SessionStates.ready);
    }

    @Override
    public void send(final IPacket packet) {
	// Added a condition to check the connection is not retrying...
	if (connection.hasErrors() || userUri == null) {
	    GWT.log("session queuing stanza" + packet, null);
	    queuedStanzas.add(packet);
	} else {
	    packet.setAttribute("from", userUri.toString());
	    eventBus.fireEvent(new BeforeStanzaSendEvent(packet));
	    connection.send(packet);
	}
    }

    @Override
    public void sendIQ(final String category, final IQ iq, final IQResponseHandler handler) {
	final String id = iqManager.register(category, handler);
	iq.setAttribute("id", id);
	send(iq);
    }

    @Override
    public void setSessionState(final String newState) {
	if (SessionStates.ready.equals(newState)) {
	    sendQueuedStanzas();
	} else if (SessionStates.disconnected.equals(newState)) {
	    userUri = null;
	}
	super.setSessionState(newState);
    }

    @Override
    public String toString() {
	return "Session " + userUri + " in " + getSessionState() + " " + queuedStanzas.size() + " queued stanzas con="
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
	this.userUri = userURI;
	GWT.log("SESSION LOGGED IN");
	setSessionState(XmppSession.SessionStates.loggedIn);
    }
}
