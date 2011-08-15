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

import java.util.Date;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.xmpp.datetime.XmppDateTime;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;

/**
 * Represents the delay of message delivery. It can be applied to any stanza. It
 * can be used both for legacy XEP-0091 and for the new XEP-0203.
 */
public class Delay {

	private final IPacket packet;

	public Delay(final IPacket packet) {
		this.packet = packet;
	}

	public XmppURI getFrom() {
		return XmppURI.uri(packet.getAttribute("from"));
	}

	public Date getStamp() {
		Date retValue = null;
		final String stamp = packet.getAttribute("stamp");
		if ("x".equals(packet.getName()) && "jabber:x:delay".equals(packet.getAttribute("xmlns"))) {
			retValue = XmppDateTime.parseLegacyFormatXMPPDateTime(stamp);
		} else if ("delay".equals(packet.getName()) && "urn:xmpp:delay".equals(packet.getAttribute("xmlns"))) {
			retValue = XmppDateTime.parseXMPPDateTime(stamp);
		}
		return retValue;
	}
}
