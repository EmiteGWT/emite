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

import java.util.Collection;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.suco.client.events.Listener;

/**
 * Create and manage chat conversations.
 * 
 * There are one implementation for one-to-one conversations (PairChatManager)
 * and many-to-many conversations (RoomManagerImpl)
 */
public interface ChatManager {
    /**
     * Close the given conversation. If a conversation is closed, a new
     * onChatCreated event will be throw when opened
     * 
     * @param chat
     */
    public void close(Chat chat);

    public Collection<? extends Chat> getChats();

    /**
     * Event sent when the chat is closed (two reasons: the user closed the chat
     * or the session disconnected)
     * 
     * @param listener
     */
    public void onChatClosed(Listener<Chat> listener);

    /**
     * Event sent when the chat is created (either by the user or by another
     * user)
     * 
     * @param listener
     */
    public void onChatCreated(Listener<Chat> listener);

    /**
     * Event sent when the user request to open the chat (either if its created
     * or not previously)
     * 
     * @param listener
     *            receives the chat as parameter
     */
    public void onChatOpened(Listener<Chat> listener);

    /**
     * Get a chat associated to the given uri. If the chat is previouly created,
     * it just returns it. If not, it creates a new Chat object and then return
     * it.
     * 
     * It's the same as open(uri, null);
     * 
     * @param uri
     *            the uri we want to chat to
     * @return the Chat object
     */
    public Chat open(XmppURI uri);

    /**
     * Get a chat associated to a given uri and a given metadata. If the no
     * associated chat is found, a new one is created.
     * 
     * @param uri
     *            the uri to chat with
     * @param metadata
     *            the metadata associated to the chat
     * @return the created chat
     */
    public Chat open(XmppURI uri, ChatMetadata metadata);

    /**
     * Use findChat instead.
     * 
     * @param uri
     * @return the chat, can be null
     * @see findChat
     */
    @Deprecated
    Chat getChat(XmppURI uri);

    /**
     * Find a previously created chat with the given characteristics (uri and
     * metadata).
     * 
     * @param uri
     *            the uri of the chat
     * @param metaddata
     *            the desired metadata of the chat. Can be ignored (see
     *            ChatSelectorStrategy)
     * @return a previously created chat if any
     */
    public Chat findChat(XmppURI uri, ChatMetadata metaddata);

    /**
     * Changes a the chat selection strategy of the current chat manager
     * 
     * @param strategy
     *            the new strategy. can't be null
     */
    void setChatSelectionStrategy(ChatSelectionStrategy strategy);

}
