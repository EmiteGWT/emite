package com.calclab.emite.im.client.chat;

import com.calclab.emite.core.client.packet.NoPacket;
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
	boolean messageHasBody = message.getFirstChild("body") != NoPacket.INSTANCE;
	properties.setShouldCreateNewChat(messageHasBody);
	return properties;
    }

    @Override
    public boolean isAssignable(final Chat chat, final ChatProperties properties) {
	return chat.getURI().equalsNoResource(properties.getUri());
    }

}
