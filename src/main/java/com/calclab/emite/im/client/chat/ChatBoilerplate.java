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
import com.calclab.emite.core.client.session.XmppSession;
import com.calclab.emite.core.client.stanzas.Message;
import com.calclab.emite.core.client.stanzas.XmppURI;
import com.calclab.emite.core.client.stanzas.Message.Type;
import com.calclab.emite.im.client.events.ChatStatusChangedEvent;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;

public abstract class ChatBoilerplate implements Chat, MessageReceivedEvent.Handler {

	private static final String PREVIOUS_CHAT_STATUS = "chatstatus.previous";

	protected final EventBus eventBus;
	protected final XmppSession session;
	protected final ChatProperties properties;

	protected ChatBoilerplate(final EventBus eventBus, final XmppSession session, final ChatProperties properties) {
		assert properties.getStatus() != null : "Status can't be null in chats";

		this.eventBus = eventBus;
		this.session = session;
		this.properties = properties;

		setPreviousChatStatus(getStatus());

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
	public HandlerRegistration addChatStatusChangedHandler(final boolean sendCurrent, final ChatStatusChangedEvent.Handler handler) {
		if (sendCurrent) {
			handler.onChatStatusChanged(new ChatStatusChangedEvent(getStatus()));
		}
		return eventBus.addHandlerToSource(ChatStatusChangedEvent.TYPE, this, handler);
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
		setStatus(ChatStatus.locked);
	}

	@Override
	public boolean isReady() {
		return ChatStatus.ready.equals(getStatus());
	}

	@Override
	public void send(final Message message) {
		if (ChatStatus.ready.equals(getStatus())) {
			message.setFrom(session.getCurrentUserURI());
			eventBus.fireEventFromSource(new BeforeMessageSentEvent(message), this);
			session.send(message);
			eventBus.fireEventFromSource(new MessageSentEvent(message), this);
		} else {
			eventBus.fireEventFromSource(new ErrorEvent(ChatErrors.sendNotReady, "The chat is not ready. You can't send messages", null), this);
		}
	}
	
	@Override
	public ChatStatus getStatus() {
		return properties.getStatus();
	}
	
	/**
	 * Set the current chat status
	 * 
	 * @param chatStatus
	 */
	protected void setStatus(final ChatStatus chatStatus) {
		assert chatStatus != null : "Chat status can't be null";
		if (!chatStatus.equals(properties.getStatus())) {
			properties.setStatus(chatStatus);
			eventBus.fireEventFromSource(new ChatStatusChangedEvent(chatStatus), this);
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

	protected ChatStatus getPreviousChatStatus() {
		return (ChatStatus) properties.getData(PREVIOUS_CHAT_STATUS);
	}

	protected void setPreviousChatStatus(final ChatStatus chatStatus) {
		properties.setData(PREVIOUS_CHAT_STATUS, chatStatus);
	}

}
