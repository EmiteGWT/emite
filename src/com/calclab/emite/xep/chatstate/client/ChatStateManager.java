/*
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008-2009 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.calclab.emite.xep.chatstate.client;

import com.calclab.emite.core.client.events.MessageEvent;
import com.calclab.emite.core.client.events.MessageHandler;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.im.client.chat.Chat;
import com.calclab.emite.xep.chatstate.client.events.ChatStateNotificationEvent;
import com.calclab.emite.xep.chatstate.client.events.ChatStateNotificationHandler;
import com.calclab.suco.client.events.Listener;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * XEP-0085: Chat State Notifications
 * http://www.xmpp.org/extensions/xep-0085.html (Version: 1.2)
 */
public class ChatStateManager {
    public static enum ChatState {
	active, composing, pause, inactive, gone
    }

    public static enum NegotiationStatus {
	notStarted, started, rejected, accepted
    }

    public static final String XMLNS = "http://jabber.org/protocol/chatstates";

    public static final String KEY = "ChatStateManager";

    private ChatState ownState;
    private ChatState otherState;
    private final Chat chat;
    private NegotiationStatus negotiationStatus;

    public ChatStateManager(final Chat chat) {
	this.chat = chat;
	negotiationStatus = NegotiationStatus.notStarted;

	chat.addMessageReceivedHandler(new MessageHandler() {
	    @Override
	    public void onMessage(MessageEvent event) {
		handleMessageReceived(chat, event.getMessage());
	    }
	});

	chat.addBeforeSendMessageHandler(new MessageHandler() {
	    @Override
	    public void onMessage(MessageEvent event) {
		beforeSendMessage(event.getMessage());
	    }
	});
    }

    public HandlerRegistration addChatStateNotificationHandler(ChatStateNotificationHandler handler) {
	return ChatStateNotificationEvent.bind(chat.getChatEventBus(), handler);
    }

    public NegotiationStatus getNegotiationStatus() {
	return negotiationStatus;
    }

    public ChatState getOtherState() {
	return otherState;
    }

    public ChatState getOwnState() {
	return ownState;
    }

    /**
     * Use addChatStateNotificationHandler
     * 
     * @param listener
     */
    @Deprecated
    public void onChatStateChanged(final Listener<ChatState> listener) {
	addChatStateNotificationHandler(new ChatStateNotificationHandler() {
	    @Override
	    public void onChatStateChanged(ChatStateNotificationEvent event) {
		listener.onEvent(event.getChatState());
	    }
	});
    }

    public void setOwnState(final ChatState chatState) {
	// From XEP: a client MUST NOT send a second instance of any given
	// standalone notification (i.e., a standalone notification MUST be
	// followed by a different state, not repetition of the same state).
	// However, every content message SHOULD contain an <active/>
	// notification.
	if (negotiationStatus.equals(NegotiationStatus.accepted)) {
	    if (ownState == null || !ownState.equals(chatState)) {
		this.ownState = chatState;
		GWT.log("Setting own status to: " + chatState.toString(), null);
		sendStateMessage(chatState);
	    }
	}
    }

    protected void handleMessageReceived(final Chat chat, final Message message) {
	for (int i = 0; i < ChatState.values().length; i++) {
	    final ChatState chatState = ChatState.values()[i];
	    final String typeSt = chatState.toString();
	    if (message.hasChild(typeSt) || message.hasChild("cha:" + typeSt)) {
		otherState = chatState;
		if (negotiationStatus.equals(NegotiationStatus.notStarted)) {
		    sendStateMessage(ChatState.active);
		}
		if (chatState.equals(ChatState.gone)) {
		    negotiationStatus = NegotiationStatus.notStarted;
		} else {
		    negotiationStatus = NegotiationStatus.accepted;
		}
		GWT.log("Receiver other chat status: " + typeSt, null);
		chat.getChatEventBus().fireEvent(new ChatStateNotificationEvent(chatState));
	    }
	}
    }

    void beforeSendMessage(final Message message) {
	switch (negotiationStatus) {
	case notStarted:
	    negotiationStatus = NegotiationStatus.started;
	case accepted:
	    boolean alreadyWithState = false;
	    for (int i = 0; i < ChatState.values().length; i++) {
		if (message.hasChild(ChatState.values()[i].toString())) {
		    alreadyWithState = true;
		}
	    }
	    if (!alreadyWithState) {
		message.addChild(ChatState.active.toString(), XMLNS);
	    }
	    break;
	case rejected:
	case started:
	    // do nothing
	    break;
	}
    }

    private void sendStateMessage(final ChatState chatState) {
	final Message message = new Message(null, chat.getURI());
	message.addChild(chatState.toString(), XMLNS);
	chat.send(message);
    }
}
