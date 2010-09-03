package com.calclab.emite.im.client.chat;

import com.calclab.emite.core.client.packet.NoPacket;
import com.calclab.emite.core.client.xmpp.stanzas.BasicStanza;

/**
 * A simple chat provider strategy. It ignores the metadata and the resource of
 * the user. This is the default strategy for a ChatManager
 * 
 */
public class PairChatSelectionStrategy implements ChatSelectionStrategy {

    @Override
    public ChatProperties extractProperties(final BasicStanza stanza) {
	ChatProperties properties = new ChatProperties(stanza.getFrom());
	boolean messageHasBody = stanza.getFirstChild("body") != NoPacket.INSTANCE;
	String stanzaType = stanza.getAttribute("type");
	// We don't support groupchat here.
	boolean isGroupChatMessage = stanzaType != null && stanzaType.equals("groupchat");
	properties.setShouldCreateNewChat(messageHasBody && !isGroupChatMessage);
	return properties;
    }

    @Override
    public boolean isAssignable(final ChatProperties chatProperties, final ChatProperties properties) {
	return chatProperties.getUri().equalsNoResource(properties.getUri());
    }

}
