/*
 * ((e)) emite: A pure Google Web Toolkit XMPP library
 * Copyright (c) 2008-2011 The Emite development team
 * 
 * This file is part of Emite.
 *
 * Emite is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * Emite is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with Emite.  If not, see <http://www.gnu.org/licenses/>.
 */

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
	public static Delay getDelay(final Stanza stanza) {
		final IPacket delayPacket = stanza.getFirstChild(new PacketMatcher() {

			@Override
			public boolean matches(final IPacket packet) {
				return "x".equals(packet.getName()) && "jabber:x:delay".equals(packet.getAttribute("xmlns")) || "delay".equals(packet.getName())
						&& "urn:xmpp:delay".equals(packet.getAttribute("xmlns"));
			}
		});
		if (delayPacket != NoPacket.INSTANCE)
			return new Delay(delayPacket);
		return null;
	}

}
