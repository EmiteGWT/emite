package com.calclab.emite.xxamples.pingpong.client;

import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.emite.im.client.chat.events.ChatChangedEvent;
import com.calclab.emite.im.client.chat.events.ChatChangedHandler;
import com.calclab.emite.xxamples.pingpong.client.PingPongDisplay.Style;

public class ChatManagerEventsSupervisor {

    public ChatManagerEventsSupervisor(ChatManager chatManager, final PingPongDisplay display) {
	chatManager.addChatChangedHandler(new ChatChangedHandler() {
	    @Override
	    public void onChatChanged(ChatChangedEvent event) {
		display.print("Chat " + event.getChat().getURI() + " changed: " + event.getChangeType(), Style.info);
	    }
	});
    }

}
