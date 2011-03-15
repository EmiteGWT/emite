package com.calclab.emite.xep.delay.client;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.NoPacket;
import com.calclab.emite.core.client.packet.PacketMatcher;
import com.calclab.emite.core.client.xmpp.stanzas.Stanza;

/**
 * Some utility methods related to delays and stanzas
 */
public class DelayHelper {
    /**
     * Get delay from stanza (if present)
     * 
     * @param stanza
     *            the stanza to get the delay from
     * @return the delay object if present, null otherwise
     */
    public static Delay getDelay(Stanza stanza) {
        IPacket delayPacket = stanza.getFirstChild(new PacketMatcher() {

            @Override
            public boolean matches(IPacket packet) {
                return "x".equals(packet.getName()) && "jabber:x:delay".equals(packet.getAttribute("xmlns")) || "delay".equals(packet.getName())
                        && "urn:xmpp:delay".equals(packet.getAttribute("xmlns"));
            }
        });
        if (delayPacket != NoPacket.INSTANCE) {
            return new Delay(delayPacket);
        }
        return null;
    }

}
