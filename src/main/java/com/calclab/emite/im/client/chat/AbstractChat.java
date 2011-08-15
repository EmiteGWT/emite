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

package com.calclab.emite.im.client.chat;

import com.calclab.emite.core.client.events.ErrorEvent;
import com.calclab.emite.core.client.events.MessageEvent;
import com.calclab.emite.core.client.events.MessageHandler;
import com.calclab.emite.core.client.events.MessageReceivedEvent;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.Message.Type;
import com.calclab.emite.im.client.chat.events.BeforeReceiveMessageEvent;
import com.calclab.emite.im.client.chat.events.BeforeSendMessageEvent;
import com.calclab.emite.im.client.chat.events.ChatStateChangedEvent;
import com.calclab.emite.im.client.chat.events.MessageSentEvent;

public abstract class AbstractChat extends ChatBoilerplate {

	public AbstractChat(final XmppSession session, final ChatProperties properties) {
		super(session, properties);
		assert properties.getState() != null : "State can't be null in chats";
		setPreviousChatState(getChatState());
		MessageReceivedEvent.bind(chatEventBus, new MessageHandler() {
			@Override
			public void onMessage(final MessageEvent event) {
				final Message message = event.getMessage();
				if (message.getType() == Type.error) {
					chatEventBus.fireEvent(new ErrorEvent(ChatErrors.errorMessage, "We received an error message", message));
				}
			}
		});
	}

	@Override
	public void close() {
		setChatState(ChatStates.locked);
	}

	@Override
	public boolean isReady() {
		return ChatStates.ready.equals(getChatState());
	}

	@Override
	public void receive(final Message message) {
		chatEventBus.fireEvent(new BeforeReceiveMessageEvent(message));
		chatEventBus.fireEvent(new MessageReceivedEvent(message));
	}

	@Override
	public void send(final Message message) {
		if (ChatStates.ready.equals(getChatState())) {
			message.setFrom(session.getCurrentUserURI());
			chatEventBus.fireEvent(new BeforeSendMessageEvent(message));
			session.send(message);
			chatEventBus.fireEvent(new MessageSentEvent(message));
		} else {
			chatEventBus.fireEvent(new ErrorEvent(ChatErrors.sendNotReady, "The chat is not ready. You can't send messages", null));
		}
	}

	/**
	 * Set the current chat state
	 * 
	 * @param chatState
	 */
	protected void setChatState(final String chatState) {
		assert chatState != null : "Chat state can't be null";
		if (!chatState.equals(properties.getState())) {
			properties.setState(chatState);
			chatEventBus.fireEvent(new ChatStateChangedEvent(chatState));
		}
	}

}
