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

import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.core.client.events.ErrorEvent;
import com.calclab.emite.core.client.events.ErrorHandler;
import com.calclab.emite.core.client.events.EventBusFactory;
import com.calclab.emite.core.client.events.MessageHandler;
import com.calclab.emite.core.client.events.MessageReceivedEvent;
import com.calclab.emite.core.client.events.StateChangedHandler;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.events.BeforeReceiveMessageEvent;
import com.calclab.emite.im.client.chat.events.BeforeSendMessageEvent;
import com.calclab.emite.im.client.chat.events.ChatStateChangedEvent;
import com.calclab.emite.im.client.chat.events.MessageSentEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public abstract class ChatBoilerplate implements Chat {
	protected final XmppSession session;
	protected final ChatProperties properties;
	protected final EmiteEventBus chatEventBus;

	private static final String PREVIOUS_CHAT_STATE = "chatstate.previous";

	public ChatBoilerplate(final XmppSession session, final ChatProperties properties) {
		this.session = session;
		this.properties = properties;
		chatEventBus = EventBusFactory.create(properties.getUri().toString());
	}

	@Override
	public HandlerRegistration addBeforeReceiveMessageHandler(final MessageHandler handler) {
		return BeforeReceiveMessageEvent.bind(chatEventBus, handler);
	}

	@Override
	public HandlerRegistration addBeforeSendMessageHandler(final MessageHandler handler) {
		return BeforeSendMessageEvent.bind(chatEventBus, handler);
	}

	@Override
	public HandlerRegistration addChatStateChangedHandler(final boolean sendCurrent, final StateChangedHandler handler) {
		if (sendCurrent) {
			handler.onStateChanged(new ChatStateChangedEvent(getChatState()));
		}
		return ChatStateChangedEvent.bind(chatEventBus, handler);
	}

	@Override
	public HandlerRegistration addErrorHandler(final ErrorHandler handler) {
		return ErrorEvent.bind(chatEventBus, handler);
	}

	@Override
	public HandlerRegistration addMessageReceivedHandler(final MessageHandler handler) {
		return MessageReceivedEvent.bind(chatEventBus, handler);
	}

	@Override
	public HandlerRegistration addMessageSentHandler(final MessageHandler handler) {
		return MessageSentEvent.bind(chatEventBus, handler);
	}

	@Override
	public EmiteEventBus getChatEventBus() {
		return chatEventBus;
	}

	@Override
	public String getChatState() {
		return properties.getState();
	}

	@Override
	public ChatProperties getProperties() {
		return properties;
	}

	@Override
	public XmppSession getSession() {
		return session;
	}

	// FIXME: rename to getUri
	@Override
	public XmppURI getURI() {
		return properties.getUri();
	}

	@Override
	public boolean isInitiatedByMe() {
		return session.getCurrentUserURI() != null && session.getCurrentUserURI().equals(properties.getInitiatorUri());
	}

	protected String getPreviousChatState() {
		return (String) properties.getData(PREVIOUS_CHAT_STATE);
	}

	protected void setPreviousChatState(final String chatState) {
		properties.setData(PREVIOUS_CHAT_STATE, chatState);
	}

}
