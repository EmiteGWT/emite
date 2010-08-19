package com.calclab.emite.xxamples.pingpong.client.logic;

import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.xxamples.pingpong.client.PingPongDisplay;
import com.calclab.emite.xxamples.pingpong.client.PingPongDisplay.Style;
import com.calclab.suco.client.Suco;
import com.calclab.suco.client.events.Listener;

/**
 * Receives pings (and answer pongs) using the old Session object
 * 
 */
public class PongSessionPresenter {

    private final XmppURI other;
    private final PingPongDisplay output;
    protected int pongs;

    public PongSessionPresenter(final XmppURI other, final PingPongDisplay output) {
	this.other = other;
	this.output = output;
	pongs = 0;
    }

    public void start() {
	output.printHeader("This is pong session example", Style.title);
	output.printHeader("Pong to: " + other, Style.info);
	output.print("You need to open the ping example page in order to run the example", Style.important);

	final Session session = Suco.get(Session.class);
	session.onMessage(new Listener<Message>() {
	    @Override
	    public void onEvent(final Message message) {
		output.print(("RECEIVED: " + message.getBody()), Style.received);
		pongs++;
		final String body = "Pong " + pongs + " [" + System.currentTimeMillis() + "]";
		session.send(new Message(body, other));
		output.print("SENT: " + body, Style.sent);
	    }
	});

    }

}
