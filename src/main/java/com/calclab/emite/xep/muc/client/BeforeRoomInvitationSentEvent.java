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

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.xmpp.stanzas.BasicStanza;
import com.google.web.bindery.event.shared.Event;

/**
 * An event to know when an invitation to a room is going to be send.
 * 
 */
public class BeforeRoomInvitationSentEvent extends Event<BeforeRoomInvitationSentEvent.Handler> {
	
	public interface Handler {
		void onBeforeRoomInvitationSent(BeforeRoomInvitationSentEvent event);
	}

	public static final Type<Handler> TYPE = new Type<Handler>();

	private final BasicStanza message;
	private final IPacket invitePacket;

	protected BeforeRoomInvitationSentEvent(final BasicStanza message, final IPacket invitePacket) {
		this.message = message;
		this.invitePacket = invitePacket;
	}

	public BasicStanza getMessage() {
		return message;
	}
	
	public IPacket getInvitePacket() {
		return invitePacket;
	}

	@Override
	public Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(final Handler handler) {
		handler.onBeforeRoomInvitationSent(this);
	}

}
