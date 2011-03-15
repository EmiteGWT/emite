package com.calclab.emite.xxamples.pingpong.client.events;

import com.calclab.emite.core.client.events.ErrorEvent;
import com.calclab.emite.core.client.events.ErrorHandler;
import com.calclab.emite.core.client.events.StateChangedEvent;
import com.calclab.emite.core.client.events.StateChangedHandler;
import com.calclab.emite.core.client.events.ChangedEvent.ChangeTypes;
import com.calclab.emite.im.client.chat.Chat;
import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.emite.im.client.chat.events.ChatChangedEvent;
import com.calclab.emite.im.client.chat.events.ChatChangedHandler;
import com.calclab.emite.xxamples.pingpong.client.PingPongDisplay;
import com.calclab.emite.xxamples.pingpong.client.PingPongDisplay.Style;
import com.google.inject.Inject;

public class ChatManagerEventsSupervisor {

    @Inject
    public ChatManagerEventsSupervisor(ChatManager chatManager, final PingPongDisplay display) {
	chatManager.addChatChangedHandler(new ChatChangedHandler() {
	    @Override
	    public void onChatChanged(ChatChangedEvent event) {
		display.print("CHAT CHANGED " + event.getChat().getURI() + " - " + event.getChangeType(), Style.event);
		if (event.is(ChangeTypes.created)) {
		    trackChat(event.getChat(), display);
		}
	    }
	});
    }

    protected void trackChat(final Chat chat, final PingPongDisplay output) {
	chat.addChatStateChangedHandler(false, new StateChangedHandler() {
	    @Override
	    public void onStateChanged(StateChangedEvent event) {
		output.print("CHAT STATE " + chat.getURI() + " changed: " + event.getState(), Style.event);
	    }
	});
	output.print("CHAT STATE " + chat.getURI() + " - " + chat.getChatState(), Style.event);

	chat.addErrorHandler(new ErrorHandler() {
	    @Override
	    public void onError(ErrorEvent event) {
		String stanza = event.getStanza() != null ? event.getStanza().toString() : "(no stanza)";
		output.print("CHAT ERROR " + chat.getURI() + ": " + event.getErrorType() + "- "
			+ event.getDescription() + ": " + stanza, Style.error);
	    }
	});
    }

}
