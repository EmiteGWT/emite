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
package com.calclab.emite.core.client.packet;

import java.util.HashMap;

public class MatcherFactory {

    private static HashMap<String, PacketMatcher> byName = new HashMap<String, PacketMatcher>();

    public static final PacketMatcher ANY = new PacketMatcher() {
	public boolean matches(final IPacket packet) {
	    return true;
	}
    };

    public static PacketMatcher byName(final String nodeName) {
	PacketMatcher matcher = byName.get(nodeName);
	if (matcher == null) {
	    matcher = new PacketMatcher() {
		public boolean matches(final IPacket packet) {
		    return nodeName.equals(packet.getName());
		}
	    };
	    byName.put(nodeName, matcher);
	}
	return matcher;
    }

    public static PacketMatcher byNameAndXMLNS(final String nodeName, final String nodeXmls) {
	return new PacketMatcher() {
	    public boolean matches(final IPacket packet) {
		return nodeName.equals(packet.getName()) && packet.hasAttribute("xmlns", nodeXmls);
	    }
	};
    }
}
