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

package com.calclab.emite.xep.muc.client.events;

import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.xmpp.stanzas.BasicStanza;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * An event to know when an invitation to a room is going to be send.
 * 
 */
public class BeforeRoomInvitationSendEvent extends GwtEvent<BeforeRoomInvitationSendHandler> {

	private static final Type<BeforeRoomInvitationSendHandler> TYPE = new Type<BeforeRoomInvitationSendHandler>();

	public static HandlerRegistration bind(final EmiteEventBus eventBus, final BeforeRoomInvitationSendHandler handler) {
		return eventBus.addHandler(TYPE, handler);
	}

	private final BasicStanza message;

	private final IPacket invitePacket;

	public BeforeRoomInvitationSendEvent(final BasicStanza message, final IPacket invitePacket) {
		this.message = message;
		this.invitePacket = invitePacket;
	}

	@Override
	public Type<BeforeRoomInvitationSendHandler> getAssociatedType() {
		return TYPE;
	}

	public IPacket getInvitePacket() {
		return invitePacket;
	}

	public BasicStanza getMessage() {
		return message;
	}

	@Override
	protected void dispatch(final BeforeRoomInvitationSendHandler handler) {
		handler.onBeforeInvitationSend(this);
	}

}
