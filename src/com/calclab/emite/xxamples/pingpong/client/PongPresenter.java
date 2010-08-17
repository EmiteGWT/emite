package com.calclab.emite.xxamples.pingpong.client;

import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.xxamples.pingpong.client.PingPongChatDisplay.Style;
import com.calclab.suco.client.Suco;
import com.calclab.suco.client.events.Listener;

/**
 * Receives pings (and answer pongs) using the old Session object
 * 
 */
public class PongPresenter {

    private final XmppURI other;
    private final PingPongChatDisplay output;
    protected int pongs;
    private final Session session;

    public PongPresenter(final XmppURI other, final PingPongChatDisplay output) {
	this.other = other;
	this.output = output;
	pongs = 0;
	session = Suco.get(Session.class);
    }

    public void start() {
	output.print("This is pong", Style.title);
	output.print("Pong to: " + other, Style.info);
	output.print("You need to open the ping example page in order to run the example", Style.important);

	// NO NEED OF LOGIN: BROWSER MODULE DOES THAT FOR US!!
	session.onStateChanged(new Listener<Session>() {
	    @Override
	    public void onEvent(final Session parameter) {
		output.print(("SESSION : " + session.getState()), Style.session);
	    }
	});

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
