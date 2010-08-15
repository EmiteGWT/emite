package com.calclab.emite.xxamples.im.pingpongchat.client;

import com.calclab.emite.core.client.events.MessageEvent;
import com.calclab.emite.core.client.events.MessageHandler;
import com.calclab.emite.core.client.events.StateChangedEvent;
import com.calclab.emite.core.client.events.StateChangedHandler;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.calclab.emite.core.client.xmpp.session.XmppSession.SessionStates;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.Chat;
import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.emite.xxamples.im.pingpongchat.client.PingPongChatDisplay.Style;
import com.calclab.suco.client.Suco;
import com.google.gwt.user.client.Timer;

public class PingChat {

    private final XmppURI other;
    private final PingPongChatDisplay output;
    private int pings;
    private int waitTime;

    public PingChat(XmppURI other, PingPongChatDisplay output) {
	this.other = other;
	this.output = output;
	pings = 0;
	waitTime = 2000;
    }

    private Chat openChat() {
	ChatManager chatManager = Suco.get(ChatManager.class);
	Chat chat = chatManager.open(other);
	chat.addMessageReceivedHandler(new MessageHandler() {
	    @Override
	    public void onMessage(MessageEvent event) {
		output.print(("RECEIVED: " + event.getMessage().getBody()), Style.received);
	    }
	});
	return chat;
    }

    protected void sendPing(final Chat chat) {
	if (chat.isReady()) {
	    pings++;
	    waitTime += 500;
	    final String body = "Ping " + pings + " [" + System.currentTimeMillis() + "]";
	    chat.send(new Message(body));
	    output.print("SENT: " + body, Style.sent);
	    new Timer() {
		@Override
		public void run() {
		    sendPing(chat);
		}
	    }.schedule(waitTime);
	}
    }

    public void start() {
	output.printHeader("This is ping", Style.title);
	output.printHeader("Ping to: " + other, Style.info);
	output.printHeader("You need to open the pong example page in order to run the example", Style.important);

	XmppSession session = Suco.get(XmppSession.class);
	// NO NEED OF LOGIN: BROWSER MODULE DOES THAT FOR US!!
	// WHEN LOGGED IN, SEND THE FIRST PING
	session.addSessionStateChangedHandler(new StateChangedHandler() {
	    @Override
	    public void onStateChanged(final StateChangedEvent event) {
		if (event.is(SessionStates.ready)) {
		    Chat chat = openChat();
		    new ChatEventsSupervisor(chat, output);
		    sendPing(chat);
		}
		output.print(("SESSION : " + event.getState()), Style.session);
	    }
	}, true);
    }

}
