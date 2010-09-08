package com.calclab.emite.xxamples.core.xmpp.session.client;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;

import com.calclab.emite.core.client.events.MessageEvent;
import com.calclab.emite.core.client.events.MessageHandler;
import com.calclab.emite.core.client.events.PresenceEvent;
import com.calclab.emite.core.client.events.PresenceHandler;
import com.calclab.emite.core.client.events.StateChangedEvent;
import com.calclab.emite.core.client.events.StateChangedHandler;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.calclab.emite.core.client.xmpp.session.XmppSession.SessionStates;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.Presence;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Xmpp Session Example
 */
public class ExampleXmppSession implements EntryPoint {

    private VerticalPanel panel;

    @Override
    public void onModuleLoad() {
	DeferredCommand.addCommand(new Command() {
	    @Override
	    public void execute() {
		panel = new VerticalPanel();
		RootPanel.get("app").add(panel);
		log("Emite example: xmpp sessions");

		/*
		 * We get the Session object using the ginjector. This is NOT
		 * the recommended way: it's better to inject into constructor
		 */
		GWT.log("Create a xmpp session");
		ExampleXmppSessionGinjector ginjector = GWT.create(ExampleXmppSessionGinjector.class);
		final XmppSession session = ginjector.getXmppSession();

		GWT.log("Add event handlers");
		/*
		 * We track session state changes. We can only send messages
		 * when the state == loggedIn.
		 */
		session.addSessionStateChangedHandler(true, new StateChangedHandler() {
		    @Override
		    public void onStateChanged(StateChangedEvent event) {
			if (event.is(SessionStates.loggedIn)) {
			    log("We are now online");
			    sendHelloWorldMessage(session);
			} else if (event.is(SessionStates.disconnected)) {
			    log("We are now offline");
			} else {
			    log("Current state: " + event.getState());
			}
		    }
		});

		/*
		 * We show every incoming message in the GWT log console
		 */
		session.addMessageReceivedHandler(new MessageHandler() {
		    @Override
		    public void onMessage(MessageEvent event) {
			Message message = event.getMessage();
			log("Messaged received from " + message.getFrom() + ":" + message.getBody());
		    }
		});

		/*
		 * We show (log) every incoming presence stanzas
		 */
		session.addPresenceReceivedHandler(new PresenceHandler() {

		    @Override
		    public void onPresence(PresenceEvent event) {
			Presence presence = event.getPresence();
			log("Presence received from " + presence.getFrom() + ": " + presence.toString());
		    }
		});
	    }
	});

    }

    private void log(final String text) {
	panel.add(new Label(text));
    }

    /**
     * The simplest way to send a message using the Session object
     */
    private void sendHelloWorldMessage(final XmppSession session) {
	final Message message = new Message("hello world!", uri("everybody@world.org"));
	session.send(message);
    }
}
