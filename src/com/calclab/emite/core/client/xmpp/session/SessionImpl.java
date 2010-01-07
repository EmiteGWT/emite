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
import java.util.Date;

import com.calclab.emite.core.client.bosh.Connection;
import com.calclab.emite.core.client.bosh.StreamSettings;
import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.xmpp.resource.ResourceBindingManager;
import com.calclab.emite.core.client.xmpp.sasl.AuthorizationTransaction;
import com.calclab.emite.core.client.xmpp.sasl.SASLManager;
import com.calclab.emite.core.client.xmpp.stanzas.IQ;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.Presence;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.suco.client.events.Listener;
import com.google.gwt.core.client.GWT;

/**
 * Default Session implementation. Use Session interface instead.
 */
public class SessionImpl extends AbstractSession implements Session {
    private State state;
    private XmppURI userURI;
    private final Connection connection;
    private AuthorizationTransaction transaction;
    private final IQManager iqManager;
    private final ArrayList<IPacket> queuedStanzas;

    public SessionImpl(final Connection connection, final SASLManager saslManager,
	    final ResourceBindingManager bindingManager, final IMSessionManager iMSessionManager) {
	this.connection = connection;
	state = State.disconnected;
	this.iqManager = new IQManager();
	this.queuedStanzas = new ArrayList<IPacket>();

	connection.onStanzaReceived(new Listener<IPacket>() {
	    public void onEvent(final IPacket stanza) {
		final String name = stanza.getName();
		if (name.equals("message")) {
		    onMessage.fire(new Message(stanza));
		} else if (name.equals("presence")) {
		    onPresence.fire(new Presence(stanza));
		} else if (name.equals("iq")) {
		    final String type = stanza.getAttribute("type");
		    if ("get".equals(type) || "set".equals(type)) {
			onIQ.fire(new IQ(stanza));
		    } else {
			iqManager.handle(stanza);
		    }
		} else if (transaction != null && "stream:features".equals(name) && stanza.hasChild("mechanisms")) {
		    saslManager.sendAuthorizationRequest(transaction);
		    transaction = null;
		}
	    }
	});

	connection.onError(new Listener<String>() {
	    public void onEvent(final String msg) {
		GWT.log("Connection error: " + msg, null);
		setState(State.error);
		disconnect();
	    }
	});

	saslManager.onAuthorized(new Listener<AuthorizationTransaction>() {
	    public void onEvent(final AuthorizationTransaction ticket) {
		if (ticket.getState() == AuthorizationTransaction.State.succeed) {
		    setState(Session.State.authorized);
		    connection.restartStream();
		    bindingManager.bindResource(ticket.uri.getResource());
		} else {
		    setState(Session.State.notAuthorized);
		    disconnect();
		}
	    }
	});

	bindingManager.onBinded(new Listener<XmppURI>() {
	    public void onEvent(final XmppURI uri) {
		iMSessionManager.requestSession(uri);
	    }
	});

	iMSessionManager.onSessionCreated(new Listener<XmppURI>() {
	    public void onEvent(final XmppURI uri) {
		setLoggedIn(uri);
	    }

	});

    }

    public XmppURI getCurrentUser() {
	return userURI;
    }

    public Session.State getState() {
	return state;
    }

    public boolean isLoggedIn() {
	return userURI != null;
    }

    public void login(XmppURI uri, final String password) {
	if (uri == Session.ANONYMOUS && password != null) {
	    throw new RuntimeException("Error on login: anonymous login can't have password");
	} else if (uri != Session.ANONYMOUS && !uri.hasResource()) {
	    uri = XmppURI.uri(uri.getNode(), uri.getHost(), "" + new Date().getTime());
	}

	if (state == Session.State.disconnected) {
	    setState(Session.State.connecting);
	    connection.connect();
	    transaction = new AuthorizationTransaction(uri, password);
	    GWT.log("Sending auth transaction: " + transaction, null);
	}
    }

    public void logout() {
	if (state != State.disconnected && userURI != null) {
	    setState(State.loggingOut);
	    userURI = null;
	    connection.disconnect();
	    setState(State.disconnected);
	}
    }

    public StreamSettings pause() {
	return connection.pause();
    }

    public void resume(final XmppURI userURI, final StreamSettings settings) {
	this.userURI = userURI;
	setState(State.resume);
	connection.resume(settings);
	setState(State.ready);
    }

    public void send(final IPacket packet) {
	if (state == State.loggedIn || state == State.ready || state == State.loggingOut) {
	    packet.setAttribute("from", userURI.toString());
	    connection.send(packet);
	} else {
	    GWT.log("session queuing stanza" + packet, null);
	    queuedStanzas.add(packet);
	}
    }

    public void sendIQ(final String category, final IQ iq, final Listener<IPacket> listener) {
	final String id = iqManager.register(category, listener);
	iq.setAttribute("id", id);
	send(iq);
    }

    public void setReady() {
	if (isLoggedIn()) {
	    setState(State.ready);
	}
    }

    private void disconnect() {
	connection.disconnect();
	setState(State.disconnected);
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
	setState(Session.State.loggedIn);
    }

    void setState(final Session.State newState) {
	this.state = newState;
	if (state == State.ready) {
	    sendQueuedStanzas();
	}
	onStateChanged.fire(state);
	onState.fire();
    }

}
