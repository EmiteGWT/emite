package com.calclab.emite.im.client.chat;

import java.util.Collection;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;

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
     * Find a chat with the given characteristics (uri and metadata) from a Chat
     * collection
     * 
     * @param uri
     *            the uri of the chat to be found
     * @param metadata
     *            the metadata of the chat to be found
     * @return the previously created chat or null if no one matches the
     *         requirements
     */
    Chat find(XmppURI uri, ChatMetadata metadata, Collection<? extends Chat> chats);

}
