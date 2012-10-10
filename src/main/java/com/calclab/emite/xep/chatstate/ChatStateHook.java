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

package com.calclab.emite.xep.chatstate;

import java.util.logging.Logger;

import com.calclab.emite.core.XmppNamespaces;
import com.calclab.emite.core.events.BeforeMessageSentEvent;
import com.calclab.emite.core.events.MessageReceivedEvent;
import com.calclab.emite.core.stanzas.Message;
import com.calclab.emite.im.chat.PairChat;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * XEP-0085: Chat State Notifications
 * http://www.xmpp.org/extensions/xep-0085.html (Version: 1.2)
 */
public final class ChatStateHook implements MessageReceivedEvent.Handler, BeforeMessageSentEvent.Handler {

	private static final Logger logger = Logger.getLogger(ChatStateHook.class.getName());

	public static enum ChatState {
		active, composing, pause, inactive, gone
	}

	public static enum NegotiationStatus {
		notStarted, started, rejected, accepted
	}

	private final EventBus eventBus;
	private final PairChat chat;

	private NegotiationStatus negotiationStatus = NegotiationStatus.notStarted;
	private ChatState ownState;
	private ChatState otherState;

	protected ChatStateHook(final EventBus eventBus, final PairChat chat) {
		this.eventBus = eventBus;
		this.chat = chat;

		chat.addMessageReceivedHandler(this);
		chat.addBeforeMessageSentHandler(this);
	}

	@Override
	public void onMessageReceived(final MessageReceivedEvent event) {
		final Message message = event.getMessage();

		for (final ChatState chatState : ChatState.values()) {
			final String typeSt = chatState.toString();
			// TODO: check namespace
			if (message.getXML().hasChild(typeSt) || message.getXML().hasChild("cha:" + typeSt)) {
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
				eventBus.fireEventFromSource(new ChatStateChangedEvent(chatState), this);
			}
		}
	}

	@Override
	public void onBeforeMessageSent(final BeforeMessageSentEvent event) {
		final Message message = event.getMessage();

		switch (negotiationStatus) {
		case notStarted:
			negotiationStatus = NegotiationStatus.started;
			//$FALL-THROUGH$
		case accepted:
			boolean alreadyWithState = false;
			for (final ChatState state : ChatState.values()) {
				if (message.getXML().hasChild(state.toString())) {
					alreadyWithState = true;
				}
			}
			if (!alreadyWithState) {
				message.getXML().addChild(ChatState.active.toString(), XmppNamespaces.CHATSTATES);
			}
			break;
		case rejected:
		case started:
			// do nothing
			break;
		}
	}

	public HandlerRegistration addChatStateChangedHandler(final ChatStateChangedEvent.Handler handler) {
		return eventBus.addHandlerToSource(ChatStateChangedEvent.TYPE, this, handler);
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

	private void sendStateMessage(final ChatState chatState) {
		final Message message = new Message();
		message.setTo(chat.getURI());
		message.getXML().addChild(chatState.toString(), XmppNamespaces.CHATSTATES);
		chat.send(message);
	}

}
