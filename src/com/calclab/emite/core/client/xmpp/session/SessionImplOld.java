package com.calclab.emite.core.client.xmpp.session;

import java.util.ArrayList;

import com.calclab.emite.core.client.bosh.StreamSettings;
import com.calclab.emite.core.client.conn.Connection;
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

public class SessionImplOld extends AbstractSession implements Session {
    private XmppURI userURI;
    private final Connection connection;
    private AuthorizationTransaction transaction;
    private final IQManager iqManager;
    private final ArrayList<IPacket> queuedStanzas;

    public SessionImplOld(final Connection connection, final SASLManager saslManager,
	    final ResourceBindingManager bindingManager, final IMSessionManager iMSessionManager) {
	this.connection = connection;
	iqManager = new IQManager();
	queuedStanzas = new ArrayList<IPacket>();

	connection.onStanzaReceived(new Listener<IPacket>() {
	    public void onEvent(final IPacket stanza) {
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
		} else if (transaction != null && "stream:features".equals(name) && stanza.hasChild("mechanisms")) {
		    saslManager.sendAuthorizationRequest(transaction.getCredentials());
		    transaction = null;
		}
	    }
	});

	connection.onError(new Listener<String>() {
	    public void onEvent(final String msg) {
		GWT.log("Connection error: " + msg, null);
		setState(State.error);
		// Connection takes care of it :
		// disconnect();
	    }
	});

	connection.onDisconnected(new Listener<String>() {
	    public void onEvent(final String parameter) {
		setState(State.disconnected);
	    }
	});

	saslManager.onAuthorized(new Listener<AuthorizationTransaction>() {
	    public void onEvent(final AuthorizationTransaction ticket) {
		if (ticket.getState() == AuthorizationTransaction.State.succeed) {
		    setState(Session.State.authorized);
		    connection.restartStream();
		    String resource = ticket.getXmppUri().getResource();
		    if (resource == null) {
			resource = "emite-" + System.currentTimeMillis();
		    }
		    bindingManager.bindResource(resource);
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

    public boolean isLoggedIn() {
	return userURI != null;
    }

    @Override
    public void login(final Credentials credentials) {
	if (getState() == Session.State.disconnected) {
	    setState(Session.State.connecting);
	    connection.connect();
	    transaction = new AuthorizationTransaction(credentials);
	    GWT.log("Sending auth transaction: " + transaction, null);
	}

    }

    public void logout() {
	if (getState() != State.disconnected && userURI != null) {
	    // TODO : To be reviewed, preventing unvailable presences to be sent
	    // so that only the 'terminate' is sent
	    // Unvailabel are handled automatically by the server
	    // setState(State.loggingOut);
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
	// Added a condition to check the connection is not retrying...
	if (!connection.hasErrors()
		&& (getState() == State.loggedIn || getState() == State.ready || getState() == State.loggingOut)) {
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

    @Override
    public String toString() {
	return "Session " + userURI + " in " + getState() + " " + queuedStanzas.size() + " queued stanzas con="
		+ connection.toString();
    }

    private void disconnect() {
	connection.disconnect();
	// Done now with the connection's onDisconnected listener :
	// setState(State.disconnected);
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
	setState(Session.State.loggedIn);
    }

    @Override
    protected void setState(final Session.State newState) {
	if (newState == State.ready) {
	    sendQueuedStanzas();
	}
	super.setState(newState);
    }
}
