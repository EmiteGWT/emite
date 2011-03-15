package com.calclab.emite.xxamples.pingpong.client.logic;

import com.calclab.emite.core.client.events.MessageEvent;
import com.calclab.emite.core.client.events.MessageHandler;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.xxamples.pingpong.client.PingPongDisplay;
import com.calclab.emite.xxamples.pingpong.client.StartablePresenter;
import com.calclab.emite.xxamples.pingpong.client.PingPongDisplay.Style;
import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * Receives pings (and answer pongs) using the old Session object
 * 
 */
public class PongSessionPresenter implements StartablePresenter {

    private final XmppURI other;
    private final PingPongDisplay output;
    protected int pongs;
    private final XmppSession session;

    @Inject
    public PongSessionPresenter(XmppSession session, @Named("other") final XmppURI other, final PingPongDisplay output) {
	this.session = session;
	this.other = other;
	this.output = output;
	pongs = 0;
    }

    public void start() {
	output.printHeader("This is pong session example", Style.title);
	output.printHeader("Pong to: " + other, Style.info);
	output.print("You need to open the ping example page in order to run the example", Style.important);

	session.addMessageReceivedHandler(new MessageHandler() {
	    @Override
	    public void onMessage(MessageEvent event) {
		Message message = event.getMessage();
		output.print(("RECEIVED: " + message.getBody()), Style.received);
		pongs++;
		final String body = "Pong " + pongs + " [" + System.currentTimeMillis() + "]";
		session.send(new Message(body, other));
		output.print("SENT: " + body, Style.sent);
	    }
	});

    }

}
