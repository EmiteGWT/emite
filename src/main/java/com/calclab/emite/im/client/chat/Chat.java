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
import com.calclab.emite.core.client.events.ErrorHandler;
import com.calclab.emite.core.client.events.MessageHandler;
import com.calclab.emite.core.client.events.StateChangedHandler;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Defines a xmpp chat.
 * 
 * This interface is implemented by PairChat and Room.
 * 
 * @see PairChat, Room
 */
public interface Chat {

	/**
	 * Add a handler to know when a message is received. It allows the listener
	 * to modify the message just before the receive event (a kind of
	 * interceptor in aop programming)
	 * 
	 * @param handler
	 *            the message handler
	 */
	public HandlerRegistration addBeforeReceiveMessageHandler(MessageHandler handler);

	/**
	 * A a handler to know when a message is going to be sent. It allows the
	 * listener to modify the message just before send it (a kind of interceptor
	 * in aop programming)
	 * 
	 * @param handler
	 *            the message handeler
	 */
	public HandlerRegistration addBeforeSendMessageHandler(MessageHandler handler);

	/**
	 * Add a handler to know whenever a chat state property changed. Normally,
	 * the states are one of the ChatStates class.
	 * 
	 * @param handler
	 * @param sendCurrentStateAsEvent
	 * 
	 * @return a registration handler in order to allow remove the event's
	 *         handler
	 * @see ChatStates
	 */
	public HandlerRegistration addChatStateChangedHandler(boolean sendCurrentState, StateChangedHandler handler);

	/**
	 * Add a handler to know when an error has occur in this chat
	 * 
	 * @param handler
	 * @return
	 */
	public HandlerRegistration addErrorHandler(ErrorHandler handler);

	/**
	 * Add a handler to know when a message is received in this chat
	 * 
	 * @param handler
	 * @return a handler registration object to detach the handler
	 */
	public HandlerRegistration addMessageReceivedHandler(MessageHandler handler);

	/**
	 * Add a handler to know when this chat has sent a message
	 * 
	 * @param handler
	 *            the message handler
	 * @return a handler registration object to detach the handler
	 * 
	 */
	public HandlerRegistration addMessageSentHandler(MessageHandler handler);

	/**
	 * Close a chat. This methos is normally called by the chat manager
	 */
	public void close();

	/**
	 * Get the event bus of this chat. Used to fire event to listeners of the
	 * chat
	 * 
	 * @return
	 */
	public EmiteEventBus getChatEventBus();

	/**
	 * Get the current chat's state
	 * 
	 * @return
	 */
	public String getChatState();

	public String getID();

	/**
	 * Get the chat properties of the chat
	 * 
	 * @return the chat properties
	 */
	public ChatProperties getProperties();

	/**
	 * Get the xmpp session associated to this chat
	 * 
	 * @return
	 */
	public XmppSession getSession();

	/**
	 * Returns this conversation URI. If this conversation is a normal chat, the
	 * uri is the JID of the other side user. If this conversation is a room,
	 * the uri is a room URI in the form of
	 * roomName@domainOfRoomService/userNickName
	 * 
	 * @return the conversation's URI
	 */
	public XmppURI getURI();

	/**
	 * Allows to know if a chat is initiated by the current user
	 * 
	 * @return Return true if you started the conversation. False otherwise
	 */
	public boolean isInitiatedByMe();

	/**
	 * Return true if the chat is ready to be used
	 * 
	 * @return
	 */
	public boolean isReady();

	/**
	 * Open a chat. This method is normally called by the chat manager
	 */
	public void open();

	/**
	 * Makes this chat receives the given message
	 * 
	 * @param message
	 *            the message to be received by this chat
	 */
	public void receive(Message message);

	/**
	 * Send a message to the uri of this chat
	 * 
	 * @param message
	 *            the message
	 * @throws RuntimeException
	 *             if chat state != ready
	 */
	public void send(Message message);

}
