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
package com.calclab.emite.core.client.xmpp.stanzas;

import java.util.Date;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.xmpp.datetime.XmppDateTime;

/**
 * Represents the delay of message delivery. It can be applied to any stanza. It
 * can be used both for legacy XEP-0091 and for the new XEP-0203.
 */
public class Delay {

    private final IPacket packet;

    public Delay(IPacket packet) {
	this.packet = packet;
    }

    public XmppURI getFrom() {
	return XmppURI.uri(packet.getAttribute("from"));
    }

    public Date getStamp() {
	Date retValue = null;
	String stamp = packet.getAttribute("stamp");
	if ("x".equals(packet.getName()) && "jabber:x:delay".equals(packet.getAttribute("xmlns"))) {
	    retValue = XmppDateTime.parseLegacyFormatXMPPDateTime(stamp);
	} else if ("delay".equals(packet.getName()) && "urn:xmpp:delay".equals(packet.getAttribute("xmlns"))) {
	    retValue = XmppDateTime.parseXMPPDateTime(stamp);
	}
	return retValue;
    }
}
