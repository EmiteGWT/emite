package com.calclab.emite.im.client.chat;

import com.calclab.emite.core.client.packet.MatcherFactory;
import com.calclab.emite.core.client.packet.NoPacket;
import com.calclab.emite.core.client.packet.PacketMatcher;
import com.calclab.emite.core.client.xmpp.stanzas.BasicStanza;

/**
 * A simple chat provider strategy. It ignores the metadata and the resource of
 * the user. This is the default strategy for a ChatManager
 * 
 */
public class PairChatSelectionStrategy implements ChatSelectionStrategy {

    private final PacketMatcher mucMatcher = MatcherFactory.byNameAndXMLNS("x", "http://jabber.org/protocol/muc#user");

    @Override
    public ChatProperties extractProperties(final BasicStanza stanza) {
        ChatProperties properties = new ChatProperties(stanza.getFrom());
        boolean messageHasBody = stanza.getFirstChild("body") != NoPacket.INSTANCE;

        boolean shouldCreate = messageHasBody && !(("groupchat").equals(stanza.getAttribute("type")));
        shouldCreate = shouldCreate && (stanza.getFirstChildInDeep(mucMatcher) == NoPacket.INSTANCE);
        properties.setShouldCreateNewChat(shouldCreate);
        return properties;
    }

    @Override
    public boolean isAssignable(final ChatProperties chatProperties, final ChatProperties properties) {
        return chatProperties.getUri().equalsNoResource(properties.getUri());
    }

}
