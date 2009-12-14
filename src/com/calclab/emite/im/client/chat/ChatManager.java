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

    public void onChatClosed(Listener<Chat> listener);

    public void onChatCreated(Listener<Chat> listener);

    /**
     * Get a chat associated to the given uri. If the chat is previouly created,
     * it just returns it. If not, it creates a new Chat object and then return
     * it.
     * 
     * @param uri
     *            the uri we want to chat to
     * @return the Chat object
     */
    public Chat open(XmppURI uri);

}
