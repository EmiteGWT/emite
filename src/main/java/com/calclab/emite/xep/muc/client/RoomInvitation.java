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
package com.calclab.emite.xep.muc.client;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.ChatProperties;

public class RoomInvitation {
    private final String reason;
    private final ChatProperties chatProperties;

    public RoomInvitation(final XmppURI invitor, final XmppURI roomURI, final String reason) {
	this.chatProperties = new ChatProperties(roomURI, invitor, null);
	this.reason = reason;
    }

    public RoomInvitation(final XmppURI invitor, final XmppURI roomURI, final String reason,
	    final ChatProperties chatProperties) {
	this.chatProperties = new ChatProperties(roomURI, invitor, null, chatProperties);
	this.reason = reason;
    }

    public ChatProperties getChatProperties() {
	return chatProperties;
    }

    public XmppURI getInvitor() {
	return chatProperties.getInitiatorUri();
    }

    public String getReason() {
	return reason;
    }

    public XmppURI getRoomURI() {
	return chatProperties.getUri();
    }

}
