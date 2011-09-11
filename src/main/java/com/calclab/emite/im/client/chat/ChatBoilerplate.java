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

import com.calclab.emite.core.client.events.BeforeMessageReceivedEvent;
import com.calclab.emite.core.client.events.BeforeMessageSentEvent;
import com.calclab.emite.core.client.events.ErrorEvent;
import com.calclab.emite.core.client.events.MessageReceivedEvent;
import com.calclab.emite.core.client.events.MessageSentEvent;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.core.client.xmpp.stanzas.Message.Type;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;

public abstract class ChatBoilerplate implements Chat, MessageReceivedEvent.Handler {

	private static final String PREVIOUS_CHAT_STATE = "chatstate.previous";

	protected final EventBus eventBus;
	protected final XmppSession session;
	protected final ChatProperties properties;

	protected ChatBoilerplate(final EventBus eventBus, final XmppSession session, final ChatProperties properties) {
		assert properties.getState() != null : "State can't be null in chats";

		this.eventBus = eventBus;
		this.session = session;
		this.properties = properties;

		setPreviousChatState(getChatState());

		addMessageReceivedHandler(this);
	}
	
	@Override
	public void onMessageReceived(final MessageReceivedEvent event) {
		final Message message = event.getMessage();
		if (message.getType() == Type.error) {
			eventBus.fireEventFromSource(new ErrorEvent(ChatErrors.errorMessage, "We received an error message", message), this);
		}
	}

	@Override
	public HandlerRegistration addBeforeMessageReceivedHandler(final BeforeMessageReceivedEvent.Handler handler) {
		return eventBus.addHandlerToSource(BeforeMessageReceivedEvent.TYPE, this, handler);
	}

	@Override
	public HandlerRegistration addBeforeMessageSentHandler(final BeforeMessageSentEvent.Handler handler) {
		return eventBus.addHandlerToSource(BeforeMessageSentEvent.TYPE, this, handler);
	}

	@Override
	public HandlerRegistration addChatStateChangedHandler(final boolean sendCurrent, final ChatStateChangedEvent.Handler handler) {
		if (sendCurrent) {
			handler.onChatStateChanged(new ChatStateChangedEvent(getChatState()));
		}
		return eventBus.addHandlerToSource(ChatStateChangedEvent.TYPE, this, handler);
	}

	@Override
	public HandlerRegistration addErrorHandler(final ErrorEvent.Handler handler) {
		return eventBus.addHandlerToSource(ErrorEvent.TYPE, this, handler);
	}

	@Override
	public HandlerRegistration addMessageReceivedHandler(final MessageReceivedEvent.Handler handler) {
		return eventBus.addHandlerToSource(MessageReceivedEvent.TYPE, this, handler);
	}

	@Override
	public HandlerRegistration addMessageSentHandler(final MessageSentEvent.Handler handler) {
		return eventBus.addHandlerToSource(MessageSentEvent.TYPE, this, handler);
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
	public void send(final Message message) {
		if (ChatStates.ready.equals(getChatState())) {
			message.setFrom(session.getCurrentUserURI());
			eventBus.fireEventFromSource(new BeforeMessageSentEvent(message), this);
			session.send(message);
			eventBus.fireEventFromSource(new MessageSentEvent(message), this);
		} else {
			eventBus.fireEventFromSource(new ErrorEvent(ChatErrors.sendNotReady, "The chat is not ready. You can't send messages", null), this);
		}
	}
	
	@Override
	public String getChatState() {
		return properties.getState();
	}
	
	/**
	 * Set the current chat state
	 * 
	 * @param chatState
	 */
	protected void setChatState(final String chatState) {
		assert chatState != null : "Chat state can't be null";
		if (!chatState.equals(properties.getState())) {
			properties.setState(chatState.toString());
			eventBus.fireEventFromSource(new ChatStateChangedEvent(chatState), this);
		}
	}

	@Override
	public ChatProperties getProperties() {
		return properties;
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
