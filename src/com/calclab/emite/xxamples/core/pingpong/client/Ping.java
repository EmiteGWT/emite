package com.calclab.emite.xxamples.core.pingpong.client;

import com.calclab.emite.core.client.events.MessageEvent;
import com.calclab.emite.core.client.events.MessageHandler;
import com.calclab.emite.core.client.events.StateChangedEvent;
import com.calclab.emite.core.client.events.StateChangedHandler;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.calclab.emite.core.client.xmpp.session.XmppSession.SessionState;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.xxamples.core.pingpong.client.PingPongEntryPoint.Output;
import com.calclab.suco.client.Suco;
import com.google.gwt.user.client.Timer;

/**
 * A simple ping example. Uses the NEW XmppSession interface.
 * 
 * @author dani
 * 
 */
public class Ping {

    private final XmppSession session;
    private final XmppURI other;
    private final Output output;
    private int pings;
    private int waitTime;

    public Ping(final XmppURI other, final Output output) {
	pings = 0;
	waitTime = 2000;
	this.other = other;
	this.output = output;
	session = Suco.get(XmppSession.class);
    }

    public void start() {
	output.print("This is ping", Output.Style.title);
	output.print("Ping to: " + other, Output.Style.info);
	output.print("You need to open the pong example page in order to run the example", Output.Style.important);

	// NO NEED OF LOGIN: BROWSER MODULE DOES THAT FOR US!!
	// WHEN LOGGED IN, SEND THE FIRST PING
	session.addSessionStateChangedHandler(new StateChangedHandler() {
	    @Override
	    public void onStateChanged(final StateChangedEvent event) {
		if (event.is(SessionState.ready)) {
		    sendPing();
		}
		output.print(("SESSION : " + event.getState()), Output.Style.session);
	    }
	}, true);

	session.addMessageReceivedHandler(new MessageHandler() {
	    @Override
	    public void onMessage(final MessageEvent event) {
		output.print(("RECEIVED: " + event.getMessage().getBody()), Output.Style.received);
	    }
	});

    }

    private void sendPing() {
	if (session.isLoggedIn()) {
	    pings++;
	    waitTime += 500;
	    final String body = "Ping " + pings + " [" + System.currentTimeMillis() + "]";
	    session.send(new Message(body, other));
	    output.print("SENT: " + body, Output.Style.sent);
	    new Timer() {
		@Override
		public void run() {
		    sendPing();
		}
	    }.schedule(waitTime);
	}
    }

}
