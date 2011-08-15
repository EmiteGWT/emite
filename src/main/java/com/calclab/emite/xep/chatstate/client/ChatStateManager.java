/*
 * ((e)) emite: A pure Google Web Toolkit XMPP library
 * Copyright (c) 2008-2011 The Emite development team
 * 
 * This file is part of Emite.
 *
 * Emite is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * Emite is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with Emite.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.calclab.emite.xep.chatstate.client;

import java.util.logging.Logger;

import com.calclab.emite.core.client.events.MessageEvent;
import com.calclab.emite.core.client.events.MessageHandler;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.im.client.chat.Chat;
import com.calclab.emite.xep.chatstate.client.events.ChatStateNotificationEvent;
import com.calclab.emite.xep.chatstate.client.events.ChatStateNotificationHandler;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * XEP-0085: Chat State Notifications
 * http://www.xmpp.org/extensions/xep-0085.html (Version: 1.2)
 */
public class ChatStateManager {
	
	private static final Logger logger = Logger.getLogger(ChatStateManager.class.getName());
	
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
			public void onMessage(final MessageEvent event) {
				handleMessageReceived(chat, event.getMessage());
			}
		});

		chat.addBeforeSendMessageHandler(new MessageHandler() {
			@Override
			public void onMessage(final MessageEvent event) {
				beforeSendMessage(event.getMessage());
			}
		});
	}

	public HandlerRegistration addChatStateNotificationHandler(final ChatStateNotificationHandler handler) {
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

	public void setOwnState(final ChatState chatState) {
		// From XEP: a client MUST NOT send a second instance of any given
		// standalone notification (i.e., a standalone notification MUST be
		// followed by a different state, not repetition of the same state).
		// However, every content message SHOULD contain an <active/>
		// notification.
		if (negotiationStatus.equals(NegotiationStatus.accepted)) {
			if (ownState == null || !ownState.equals(chatState)) {
				ownState = chatState;
				logger.info("Setting own status to: " + chatState.toString());
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
				logger.info("Receiver other chat status: " + typeSt);
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
