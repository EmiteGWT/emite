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
package com.calclab.emite.im.client.chat;

import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.core.client.events.ErrorHandler;
import com.calclab.emite.core.client.events.MessageHandler;
import com.calclab.emite.core.client.events.StateChangedHandler;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.suco.client.events.Listener;
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
     * Possible conversation states. Enum can't be extended so, this won't be
     * used anymore
     * 
     * Use ChatStates
     */
    @Deprecated
    public static enum State {
        ready, locked,
        /**
         * Because the new extensible chat state system (using strings instead
         * of enums) this is used when a unknown (not in this enum) state is set
         */
        unknown
    }

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

    /**
     * USE getProperties.getData
     */
    @Deprecated
    public <T> T getData(Class<T> type);

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
     * Use getChatState
     * 
     * @return
     */
    @Deprecated
    public State getState();

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

    public void onBeforeReceive(Listener<Message> listener);

    /**
     * A listener to know when a message is going to be sent. It allows the
     * listener to modify the message just before send it (a kind of interceptor
     * in aop programming)
     * 
     * USE addBeforeReceiveMessageHandler instead
     * 
     * @param listener
     *            the listener
     * 
     */
    @Deprecated
    public void onBeforeSend(Listener<Message> listener);

    /**
     * Allows to modify the message just before inform about the reception
     * 
     * Use addMessageReceivedHandler instead
     * 
     * @param messageInterceptor
     */
    @Deprecated
    public void onMessageReceived(Listener<Message> listener);

    /**
     * Attach a listener to know when a message has been sent
     * 
     * Use addMessageSentHandler instead
     * 
     * @param listener
     *            the listener to the events
     */
    @Deprecated
    public void onMessageSent(Listener<Message> listener);

    /**
     * Add a listener to know when the chat state changed. You should send
     * messages while the state != ready
     * 
     * Use addChatStateChangedHandler instead
     * 
     * @param listener
     */
    @Deprecated
    public void onStateChanged(Listener<State> listener);

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

    /**
     * Associate a object to this conversation.
     * 
     * @param <T>
     *            the class of the object
     * @param type
     *            the class object itself
     * @param data
     *            the object you want to associate
     * @return the object associated
     * @see getData
     */
    @Deprecated
    public <T> T setData(Class<T> type, T data);
}
