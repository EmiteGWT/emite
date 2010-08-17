package com.calclab.emite.xxamples.pingpong.client;

import com.calclab.emite.core.client.events.ChangedEvent.ChangeEventTypes;
import com.calclab.emite.core.client.events.MessageEvent;
import com.calclab.emite.core.client.events.MessageHandler;
import com.calclab.emite.core.client.events.StateChangedEvent;
import com.calclab.emite.core.client.events.StateChangedHandler;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.im.client.chat.Chat;
import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.emite.im.client.chat.events.ChatChangedEvent;
import com.calclab.emite.im.client.chat.events.ChatChangedHandler;
import com.calclab.emite.xxamples.pingpong.client.PingPongChatDisplay.Style;
import com.calclab.suco.client.Suco;

public class PongChatPresenter {

    private final PingPongChatDisplay output;
    private int pongs;

    public PongChatPresenter(PingPongChatDisplay output) {
	this.output = output;
	this.pongs = 0;
    }

    private void listenToChat(final Chat chat) {
	chat.addMessageReceivedHandler(new MessageHandler() {
	    @Override
	    public void onMessage(MessageEvent event) {
		Message message = event.getMessage();
		output.print(("RECEIVED: " + message.getBody()), Style.received);
		pongs++;
		final String body = "Pong " + pongs + " [" + System.currentTimeMillis() + "]";
		chat.send(new Message(body));
		output.print("SENT: " + body, Style.sent);

	    }
	});
    }

    public void start() {
	output.printHeader("This is pong chat", Style.title);
	output.printHeader("You need to open the ping chat example page in order to run the example", Style.important);

	XmppSession session = Suco.get(XmppSession.class);
	// NO NEED OF LOGIN: BROWSER MODULE DOES THAT FOR US!!
	session.addSessionStateChangedHandler(new StateChangedHandler() {
	    @Override
	    public void onStateChanged(final StateChangedEvent event) {
		output.print(("SESSION : " + event.getState()), Style.session);
	    }
	}, true);

	ChatManager chatManager = Suco.get(ChatManager.class);
	chatManager.addChatChangedHandler(new ChatChangedHandler() {
	    @Override
	    public void onChatChanged(ChatChangedEvent event) {
		if (event.is(ChangeEventTypes.created)) {
		    Chat chat = event.getChat();
		    new ChatEventsSupervisor(chat, output);
		    listenToChat(chat);
		}
	    }
	});
    }

}
