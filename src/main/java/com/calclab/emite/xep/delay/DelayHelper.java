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

package com.calclab.emite.xep.delay;

import com.calclab.emite.base.xml.XMLMatcher;
import com.calclab.emite.base.xml.XMLPacket;
import com.calclab.emite.core.stanzas.Stanza;

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
		final XMLPacket delayPacket = stanza.getXML().getFirstChild(new XMLMatcher() {
			@Override
			public boolean matches(final XMLPacket packet) {
				return "x".equals(packet.getTagName()) && "jabber:x:delay".equals(packet.getNamespace()) || "delay".equals(packet.getTagName()) && "urn:xmpp:delay".equals(packet.getNamespace());
			}
		});

		if (delayPacket == null)
			return null;

		return new Delay(delayPacket);
	}
	
	private DelayHelper() {
	}

}
