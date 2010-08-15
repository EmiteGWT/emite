package com.calclab.emite.xep.muc.client;

import com.calclab.emite.core.client.xmpp.stanzas.BasicStanza;
import com.calclab.emite.im.client.chat.Chat;
import com.calclab.emite.im.client.chat.ChatProperties;
import com.calclab.emite.im.client.chat.ChatSelectionStrategy;

public class RoomChatSelectionStrategy implements ChatSelectionStrategy {

    @Override
    public ChatProperties extractChatProperties(BasicStanza stanza) {
	ChatProperties properties = new ChatProperties(stanza.getFrom().getJID());
	boolean isGroupChatMessage = stanza.getAttribute("type").equals("groupchat");
	properties.setShouldCreateNewChat(isGroupChatMessage);
	return properties;
    }

    @Override
    public boolean isAssignable(Chat chat, ChatProperties properties) {
	return chat.getURI().equalsNoResource(properties.getUri());
    }

}
