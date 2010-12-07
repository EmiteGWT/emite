/*
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008-2009 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.calclab.emite.xep.delay.client;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.NoPacket;
import com.calclab.emite.core.client.packet.PacketMatcher;
import com.calclab.emite.core.client.xmpp.stanzas.Stanza;

/**
 * Default implementation of {@link DelayManager}.
 * 
 * USE: DelayHelper directly
 */
@Deprecated
public class DelayManagerImpl implements DelayManager {

    @Override
    public Delay getDelay(Stanza stanza) {
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
