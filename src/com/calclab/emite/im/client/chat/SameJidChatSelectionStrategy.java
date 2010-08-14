package com.calclab.emite.im.client.chat;

import java.util.Collection;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;

/**
 * A simple chat provider strategy. It ignores the metadata and the resource of
 * the user. This is the default strategy for a ChatManager
 * 
 */
public class SameJidChatSelectionStrategy implements ChatSelectionStrategy {

    @Override
    public Chat find(XmppURI uri, ChatMetadata metadata, Collection<? extends Chat> chats) {
	for (final Chat chat : chats) {
	    final XmppURI chatTargetURI = chat.getURI();
	    if (uri.equalsNoResource(chatTargetURI)) {
		return chat;
	    }
	}
	return null;

    }

}
