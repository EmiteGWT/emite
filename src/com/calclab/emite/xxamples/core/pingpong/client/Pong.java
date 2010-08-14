package com.calclab.emite.xxamples.core.pingpong.client;

import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.xxamples.core.pingpong.client.PingPongEntryPoint.Output;
import com.calclab.suco.client.Suco;
import com.calclab.suco.client.events.Listener;

/**
 * Simple pong example. Uses the OLD Session interface
 * 
 * @author dani
 * 
 */
public class Pong {

    private final XmppURI other;
    private final Output output;
    protected int pongs;
    private final Session session;

    public Pong(final XmppURI other, final Output output) {
	this.other = other;
	this.output = output;
	pongs = 0;
	session = Suco.get(Session.class);
    }

    public void start() {
	output.print("This is pong", Output.Style.title);
	output.print("Pong to: " + other, Output.Style.info);
	output.print("You need to open the ping example page in order to run the example", Output.Style.important);

	// NO NEED OF LOGIN: BROWSER MODULE DOES THAT FOR US!!
	session.onStateChanged(new Listener<Session>() {
	    @Override
	    public void onEvent(final Session parameter) {
		output.print(("SESSION : " + session.getState()), Output.Style.session);
	    }
	});

	session.onMessage(new Listener<Message>() {
	    @Override
	    public void onEvent(final Message message) {
		output.print(("RECEIVED: " + message.getBody()), Output.Style.received);
		pongs++;
		final String body = "Pong " + pongs + " [" + System.currentTimeMillis() + "]";
		session.send(new Message(body, other));
		output.print("SENT: " + body, Output.Style.sent);
	    }
	});

    }

}
