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

package com.calclab.emite.core.client.packet;

import java.util.HashMap;

public class MatcherFactory {

	private static HashMap<String, PacketMatcher> byName = new HashMap<String, PacketMatcher>();

	public static final PacketMatcher ANY = new PacketMatcher() {
		@Override
		public boolean matches(final IPacket packet) {
			return true;
		}
	};

	public static PacketMatcher byName(final String nodeName) {
		PacketMatcher matcher = byName.get(nodeName);
		if (matcher == null) {
			matcher = new PacketMatcher() {
				@Override
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
			@Override
			public boolean matches(final IPacket packet) {
				return nodeName.equals(packet.getName()) && packet.hasAttribute("xmlns", nodeXmls);
			}
		};
	}
}
