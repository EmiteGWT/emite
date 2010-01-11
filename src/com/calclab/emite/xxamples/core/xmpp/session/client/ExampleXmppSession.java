package com.calclab.emite.xxamples.core.xmpp.session.client;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;

import com.calclab.emite.core.client.bosh.BoshSettings;
import com.calclab.emite.core.client.bosh.Connection;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.session.Session.State;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.Presence;
import com.calclab.suco.client.Suco;
import com.calclab.suco.client.events.Listener;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Xmpp Session Example
 */
public class ExampleXmppSession implements EntryPoint {
    private VerticalPanel panel;

    public void onModuleLoad() {
	panel = new VerticalPanel();
	RootPanel.get("app").add(panel);
	log("Emite example: xmpp sessions");

	Suco.get(Connection.class).setSettings(new BoshSettings("/proxy", "localhost"));
	/*
	 * We get the Session object. The most important object of Emite Core
	 * module.
	 */
	final Session session = Suco.get(Session.class);

	/*
	 * We track session state changes. We can only send messages when the
	 * state == loggedIn.
	 */
	session.onStateChanged(new Listener<Session>() {
	    @Override
	    public void onEvent(Session session) {
		State state = session.getState();
		if (state == Session.State.loggedIn) {
		    log("We are now online");
		    sendHelloWorldMessage(session);
		} else if (state == Session.State.disconnected) {
		    log("We are now offline");
		} else {
		    log("Current state: " + state);
		}
	    }
	});

	/*
	 * We show every incoming message in the GWT log console
	 */
	session.onMessage(new Listener<Message>() {
	    public void onEvent(final Message message) {
		log("Messaged received from " + message.getFrom() + ":" + message.getBody());
	    }
	});

	/*
	 * We show (log) every incoming presence stanzas
	 */
	session.onPresence(new Listener<Presence>() {
	    public void onEvent(final Presence presence) {
		log("Presence received from " + presence.getFrom() + ": " + presence.toString());
	    }
	});

	session.login(uri("test1@localhost"), "test1");
    }

    private void log(String text) {
	panel.add(new Label(text));
    }

    /**
     * The simplest way to send a message using the Session object
     */
    private void sendHelloWorldMessage(final Session session) {
	final Message message = new Message("hello world!", uri("everybody@world.org"));
	session.send(message);
    }
}
