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

package com.calclab.emite.im.chat;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nullable;

import com.calclab.emite.core.XmppURI;
import com.calclab.emite.core.events.BeforeMessageReceivedEvent;
import com.calclab.emite.core.events.BeforeMessageSentEvent;
import com.calclab.emite.core.events.MessageReceivedEvent;
import com.calclab.emite.core.events.MessageSentEvent;
import com.calclab.emite.core.session.XmppSession;
import com.calclab.emite.core.stanzas.Message;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * Default Chat implementation. Use Chat interface instead. Created by a chat
 * manager
 * 
 * About Chat ids: Other sender Uri plus thread identifies a chat (associated
 * with a chat panel in the UI). If no thread is specified, we join all messages
 * in one chat.
 * 
 * @see Chat
 */
public final class PairChat {
	
	private static enum ChatStatus {
		ready, locked;
	}
	
	private final PairChatManagerImpl chatManager;
	private final EventBus eventBus;
	private final XmppSession session;
	
	private final XmppURI uri;
	private final XmppURI initiatorUri;
	private ChatStatus status;

	@Nullable private String thread;

	/**
	 * Create a new pair chat. Pair chats are created by PairChatManager
	 * 
	 * @param session
	 * @param properties
	 */
	protected PairChat(final PairChatManagerImpl chatManager, final EventBus eventBus, final XmppSession session, final XmppURI uri, final XmppURI initiatorUri) {
		this.chatManager = checkNotNull(chatManager);
		this.eventBus = checkNotNull(eventBus);
		this.session = checkNotNull(session);
		
		this.uri = checkNotNull(uri);
		this.initiatorUri = checkNotNull(initiatorUri);
		this.status = ChatStatus.locked;
	}
	
	protected void receiveMessage(final Message message) {
		eventBus.fireEventFromSource(new BeforeMessageReceivedEvent(message), this);
		eventBus.fireEventFromSource(new MessageReceivedEvent(message), this);
	}
	
	/**
	 * Add a handler to know when a message is received. It allows the listener
	 * to modify the message just before the receive event (a kind of
	 * interceptor in aop programming)
	 * 
	 * @param handler
	 *            the message handler
	 */
	public final HandlerRegistration addBeforeMessageReceivedHandler(final BeforeMessageReceivedEvent.Handler handler) {
		return eventBus.addHandlerToSource(BeforeMessageReceivedEvent.TYPE, this, handler);
	}
	
	/**
	 * Add a handler to know when a message is received in this chat
	 * 
	 * @param handler
	 * @return a handler registration object to detach the handler
	 */
	public final HandlerRegistration addMessageReceivedHandler(final MessageReceivedEvent.Handler handler) {
		return eventBus.addHandlerToSource(MessageReceivedEvent.TYPE, this, handler);
	}

	/**
	 * A a handler to know when a message is going to be sent. It allows the
	 * listener to modify the message just before send it (a kind of interceptor
	 * in aop programming)
	 * 
	 * @param handler
	 *            the message handler
	 */
	public final HandlerRegistration addBeforeMessageSentHandler(final BeforeMessageSentEvent.Handler handler) {
		return eventBus.addHandlerToSource(BeforeMessageSentEvent.TYPE, this, handler);
	}
	
	/**
	 * Add a handler to know when this chat has sent a message
	 * 
	 * @param handler
	 *            the message handler
	 * @return a handler registration object to detach the handler
	 * 
	 */
	public final HandlerRegistration addMessageSentHandler(final MessageSentEvent.Handler handler) {
		return eventBus.addHandlerToSource(MessageSentEvent.TYPE, this, handler);
	}
	
	protected void open() {
		if (session.isReady()) {
			status = ChatStatus.ready;
		}
	}
	
	/**
	 * Close a chat. This method is normally called by the chat manager
	 */
	public void close() {
		this.status = ChatStatus.locked;
		chatManager.closeChat(this);
	}

	/**
	 * Return true if the chat is ready to be used
	 * 
	 * @return
	 */
	public boolean isReady() {
		return ChatStatus.ready.equals(status);
	}

	public String getThread() {
		return thread;
	}
	
	public void setThread(final String thread) {
		this.thread = thread;
	}

	/**
	 * Send a message to the uri of this chat
	 * 
	 * @param message
	 *            the message
	 * @throws RuntimeException
	 *             if chat status != ready
	 */
	public void send(final Message message) {
		message.setTo(uri);
		message.setType(Message.Type.chat);
		message.setThread(thread);

		eventBus.fireEventFromSource(new BeforeMessageSentEvent(message), this);
		session.send(message);
		eventBus.fireEventFromSource(new MessageSentEvent(message), this);
	}
	
	/**
	 * Returns this conversation URI. If this conversation is a normal chat, the
	 * uri is the JID of the other side user.
	 * 
	 * @return the conversation's URI
	 */
	public XmppURI getURI() {
		return uri;
	}
	
	/**
	 * This is the uri of the entity that inititated the chat
	 * 
	 * @return
	 */
	public XmppURI getInitiatorUri() {
		return initiatorUri;
	}
	
	/**
	 * Allows to know if a chat is initiated by the current user
	 * 
	 * @return Return true if you started the conversation. False otherwise
	 */
	public boolean isInitiatedByMe() {
		return initiatorUri.equals(session.getCurrentUserURI());
	}
	
	@Override
	public String toString() {
		return "Chat: " + uri.toString() + "-" + thread;
	}

}
