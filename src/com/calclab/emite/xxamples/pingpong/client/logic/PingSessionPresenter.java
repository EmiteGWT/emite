package com.calclab.emite.xxamples.pingpong.client.logic;

import com.calclab.emite.core.client.events.MessageEvent;
import com.calclab.emite.core.client.events.MessageHandler;
import com.calclab.emite.core.client.events.StateChangedEvent;
import com.calclab.emite.core.client.events.StateChangedHandler;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.calclab.emite.core.client.xmpp.session.XmppSession.SessionStates;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.xxamples.pingpong.client.PingPongDisplay;
import com.calclab.emite.xxamples.pingpong.client.PingPongDisplay.Style;
import com.calclab.suco.client.Suco;
import com.google.gwt.user.client.Timer;

/**
 * Send pings to other jid using the XmppSession directly
 */
public class PingSessionPresenter {
    private final XmppSession session;
    private final XmppURI other;
    private final PingPongDisplay output;
    private int pings;
    private int waitTime;

    public PingSessionPresenter(final XmppURI other, final PingPongDisplay output) {
	pings = 0;
	waitTime = 2000;
	this.other = other;
	this.output = output;
	session = Suco.get(XmppSession.class);
    }

    public void start() {
	output.printHeader("This is ping example", Style.title);
	output.printHeader("Ping to: " + other, Style.info);
	output.printHeader("You need to open the pong example page in order to run the example", Style.important);

	// WHEN LOGGED IN, SEND THE FIRST PING
	session.addSessionStateChangedHandler(new StateChangedHandler() {
	    @Override
	    public void onStateChanged(final StateChangedEvent event) {
		if (event.is(SessionStates.ready)) {
		    sendPing();
		}
	    }
	}, true);

	session.addMessageReceivedHandler(new MessageHandler() {
	    @Override
	    public void onMessage(final MessageEvent event) {
		output.print(("RECEIVED: " + event.getMessage().getBody()), Style.received);
	    }
	});

    }

    private void sendPing() {
	if (session.isReady()) {
	    pings++;
	    waitTime += 500;
	    final String body = "Ping " + pings + " [" + System.currentTimeMillis() + "]";
	    session.send(new Message(body, other));
	    output.print("SENT: " + body, Style.sent);
	    new Timer() {
		@Override
		public void run() {
		    sendPing();
		}
	    }.schedule(waitTime);
	}
    }
}
