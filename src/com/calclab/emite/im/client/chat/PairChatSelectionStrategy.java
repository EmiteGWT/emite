package com.calclab.emite.im.client.chat;

import com.calclab.emite.core.client.xmpp.stanzas.Message;

/**
 * A simple chat provider strategy. It ignores the metadata and the resource of
 * the user. This is the default strategy for a ChatManager
 * 
 */
public class PairChatSelectionStrategy implements ChatSelectionStrategy {

    @Override
    public ChatProperties extractChatProperties(final Message message) {
	ChatProperties properties = new ChatProperties(message.getFrom());
	return properties;
    }

    @Override
    public boolean isAssignable(final Chat chat, final ChatProperties properties) {
	return chat.getURI().equalsNoResource(properties.getUri());
    }

}
