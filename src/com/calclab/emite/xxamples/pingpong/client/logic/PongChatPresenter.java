package com.calclab.emite.xxamples.pingpong.client.logic;

import com.calclab.emite.core.client.events.MessageEvent;
import com.calclab.emite.core.client.events.MessageHandler;
import com.calclab.emite.core.client.events.ChangedEvent.ChangeTypes;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.im.client.chat.Chat;
import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.emite.im.client.chat.events.ChatChangedEvent;
import com.calclab.emite.im.client.chat.events.ChatChangedHandler;
import com.calclab.emite.xxamples.pingpong.client.PingPongDisplay;
import com.calclab.emite.xxamples.pingpong.client.StartablePresenter;
import com.calclab.emite.xxamples.pingpong.client.PingPongDisplay.Style;
import com.calclab.emite.xxamples.pingpong.client.events.ChatManagerEventsSupervisor;
import com.google.inject.Inject;

public class PongChatPresenter implements StartablePresenter {

    private final PingPongDisplay display;
    private int pongs;
    private final ChatManager chatManager;

    @Inject
    public PongChatPresenter(ChatManager chatManager, PingPongDisplay output) {
	this.chatManager = chatManager;
	this.display = output;
	this.pongs = 0;
    }

    public void start() {
	display.printHeader("This is pong chat", Style.title);
	display.printHeader("You need to open the ping chat example page in order to run the example", Style.important);

	new ChatManagerEventsSupervisor(chatManager, display);
	chatManager.addChatChangedHandler(new ChatChangedHandler() {
	    @Override
	    public void onChatChanged(ChatChangedEvent event) {
		if (event.is(ChangeTypes.created)) {
		    Chat chat = event.getChat();
		    listenToChat(chat);
		}
	    }
	});
    }

    private void listenToChat(final Chat chat) {
	chat.addMessageReceivedHandler(new MessageHandler() {
	    @Override
	    public void onMessage(MessageEvent event) {
		Message message = event.getMessage();
		display.print(("RECEIVED: " + message.getBody()), Style.received);
		pongs++;
		final String body = "Pong " + pongs + " [" + System.currentTimeMillis() + "]";
		chat.send(new Message(body));
		display.print("SENT: " + body, Style.sent);

	    }
	});
    }

}
