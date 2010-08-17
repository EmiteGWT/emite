package com.calclab.emite.xxamples.pingpong.client.logic;

import com.calclab.emite.core.client.events.MessageEvent;
import com.calclab.emite.core.client.events.MessageHandler;
import com.calclab.emite.core.client.events.StateChangedEvent;
import com.calclab.emite.core.client.events.StateChangedHandler;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.Chat;
import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.emite.im.client.chat.Chat.ChatStates;
import com.calclab.emite.xxamples.pingpong.client.PingPongDisplay;
import com.calclab.emite.xxamples.pingpong.client.PingPongDisplay.Style;
import com.calclab.emite.xxamples.pingpong.client.events.ChatManagerEventsSupervisor;
import com.calclab.suco.client.Suco;
import com.google.gwt.user.client.Timer;

public class PingChatPresenter {

    protected final XmppURI other;
    protected final PingPongDisplay display;
    protected int pings;
    protected int waitTime;

    public PingChatPresenter(XmppURI other, PingPongDisplay output) {
	this.other = other;
	this.display = output;
	pings = 0;
	waitTime = 2000;
    }

    public void start() {
	// OPEN THE CHAT
	ChatManager chatManager = getChatManager();

	display.printHeader("Ping to: " + other, Style.info);

	new ChatManagerEventsSupervisor(chatManager, display);
	final Chat chat = chatManager.open(other);

	chat.addMessageReceivedHandler(new MessageHandler() {
	    @Override
	    public void onMessage(MessageEvent event) {
		display.print(("RECEIVED: " + event.getMessage().getBody()), Style.received);
	    }
	});

	// SEND THE FIRST PING WHEN THE CHAT IS READY
	chat.addChatStateChangedHandler(new StateChangedHandler() {
	    @Override
	    public void onStateChanged(StateChangedEvent event) {
		if (event.is(ChatStates.ready)) {
		    sendPing(chat);
		}
	    }

	}, true);
    }

    protected ChatManager getChatManager() {
	display.printHeader("This is ping chat example", Style.title);
	display.printHeader("You need to open the pong example page in order to run the example", Style.important);
	return Suco.get(ChatManager.class);
    }

    protected void sendPing(final Chat chat) {
	if (chat.isReady()) {
	    pings++;
	    waitTime += 500;
	    final String body = "Ping " + pings + " [" + System.currentTimeMillis() + "]";
	    chat.send(new Message(body));
	    display.print("SENT: " + body, Style.sent);
	    new Timer() {
		@Override
		public void run() {
		    sendPing(chat);
		}
	    }.schedule(waitTime);
	}
    }

}
