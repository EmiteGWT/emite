package com.calclab.emite.xep.muc.client;

import com.calclab.emite.core.client.xmpp.stanzas.BasicStanza;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.ChatProperties;
import com.calclab.emite.im.client.chat.ChatSelectionStrategy;

public class RoomChatSelectionStrategy implements ChatSelectionStrategy {

    @Override
    public ChatProperties extractProperties(BasicStanza stanza) {
	XmppURI from = stanza.getFrom();
	ChatProperties properties = new ChatProperties(from != null ? from.getJID() : null);
	String stanzaType = stanza.getAttribute("type");
	boolean isGroupChatMessage = stanzaType != null && stanzaType.equals("groupchat");
	properties.setShouldCreateNewChat(isGroupChatMessage);
	return properties;
    }

    @Override
    public boolean isAssignable(ChatProperties chatProperties, ChatProperties messsageProperties) {
	return chatProperties.getUri().equalsNoResource(messsageProperties.getUri());
    }

}
