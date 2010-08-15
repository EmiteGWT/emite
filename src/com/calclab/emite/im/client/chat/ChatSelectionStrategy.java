package com.calclab.emite.im.client.chat;

import com.calclab.emite.core.client.xmpp.stanzas.Message;

/**
 * An interchangeable strategy to retrieve or create new chats. You can replace
 * one strategy with other in the ChatManager.
 * 
 * The strategy uses two parameters to identify a chat: the uri and the
 * metadata. The metadata can be ignored.
 * 
 */
public interface ChatSelectionStrategy {

    /**
     * Extract the properties of the target chat of this packet. This properties
     * are used to find a chat or decide whenever or not create a new one
     * 
     * @param packet
     *            the packet to extract the properties from (usually a message,
     *            but can be presence in rooms)
     * @return the properties of the ideal receiver of this message (stanza). If
     *         null is return, the ChatManager assume this message has no
     *         receiver
     */
    ChatProperties extractChatProperties(Message message);

    /**
     * This is the used to locate a chat in a pool of chats.
     * 
     * Should return true if the given chat correspond (or is asignable) to the
     * current properties. This NOT mean the properties should be same between
     * both. Different "strategies" could be implemented (ie. only comparing the
     * uri)
     * 
     * @param chat
     *            the chat to examined
     * @param properties
     *            the properties
     * @return true if the chat matches the properties, else otherwise
     */
    boolean isAssignable(Chat chat, ChatProperties properties);

}
