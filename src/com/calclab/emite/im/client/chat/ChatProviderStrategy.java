package com.calclab.emite.im.client.chat;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;

/**
 * An interchangeable strategy to retrieve or create new chats. You can replace
 * one strategy with other in the ChatManager
 */
public interface ChatProviderStrategy {
    /**
     * Retrieve a previously created chat if someone matches the given uri and
     * metadata. Return null if no previously created chat is found.
     * 
     * @param uri
     * @param metadata
     * @return the previously created chat or null if no one matches the
     *         requirements
     */
    Chat get(XmppURI uri, ChatMetadata metadata);

    /**
     * Create a new chat with the given uri and metadata
     * 
     * @param uri
     * @param metadata
     * @return The created chat
     */
    Chat create(XmppURI uri, ChatMetadata metadata);
}
