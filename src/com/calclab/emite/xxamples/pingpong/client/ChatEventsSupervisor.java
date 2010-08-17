package com.calclab.emite.xxamples.pingpong.client;

import com.calclab.emite.core.client.events.StateChangedEvent;
import com.calclab.emite.core.client.events.StateChangedHandler;
import com.calclab.emite.im.client.chat.Chat;
import com.calclab.emite.xxamples.pingpong.client.PingPongDisplay.Style;

public class ChatEventsSupervisor {

    public ChatEventsSupervisor(final Chat chat, final PingPongDisplay output) {
	chat.addChatStateChangedHandler(new StateChangedHandler() {
	    @Override
	    public void onStateChanged(StateChangedEvent event) {
		output.print("Chat " + chat.getURI() + " state changed: " + event.getState(), Style.info);
	    }
	}, false);
	output.print("Chat " + chat.getURI() + " state: " + chat.getChatState(), Style.info);
    }

}
