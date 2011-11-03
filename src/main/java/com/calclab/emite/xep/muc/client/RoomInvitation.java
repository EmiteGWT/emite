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

package com.calclab.emite.xep.muc.client;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.concurrent.Immutable;

import com.calclab.emite.core.client.uri.XmppURI;

@Immutable
public final class RoomInvitation {

	private final XmppURI invitor;
	private final XmppURI roomURI;
	private final String reason;

	public RoomInvitation(final XmppURI invitor, final XmppURI roomURI, final String reason) {
		this.invitor = checkNotNull(invitor);
		this.roomURI = checkNotNull(roomURI);
		this.reason = reason;
	}

	public final XmppURI getRoomURI() {
		return roomURI;
	}

	public final XmppURI getInvitor() {
		return invitor;
	}

	public final String getReason() {
		return reason;
	}

}
