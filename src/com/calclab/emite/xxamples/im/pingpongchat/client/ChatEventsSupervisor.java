package com.calclab.emite.xxamples.im.pingpongchat.client;

import com.calclab.emite.core.client.events.StateChangedEvent;
import com.calclab.emite.core.client.events.StateChangedHandler;
import com.calclab.emite.im.client.chat.Chat;
import com.calclab.emite.xxamples.im.pingpongchat.client.PingPongChatDisplay.Style;

public class ChatEventsSupervisor {

    public ChatEventsSupervisor(Chat chat, final PingPongChatDisplay output) {
	chat.addChatStateChangedHandler(new StateChangedHandler() {
	    @Override
	    public void onStateChanged(StateChangedEvent event) {
		output.print("Chat state changed: " + event.getState(), Style.error);
	    }
	});
	output.print("Chat state: " + chat.getChatState(), Style.error);
    }

}
