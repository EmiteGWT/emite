package com.calclab.emite.xep.muc.client;

import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.im.client.chat.Chat;
import com.calclab.emite.im.client.chat.ChatProperties;
import com.calclab.emite.im.client.chat.ChatSelectionStrategy;

public class RoomChatSelectionStrategy implements ChatSelectionStrategy {

    @Override
    public ChatProperties extractChatProperties(Message message) {
	if (message.getType() == Message.Type.groupchat) {
	    return new ChatProperties(message.getFrom().getJID());
	}
	return null;
    }

    @Override
    public boolean isAssignable(Chat chat, ChatProperties properties) {
	return properties != null && chat.getURI().equalsNoResource(properties.getUri());
    }

}
