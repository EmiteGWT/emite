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

package com.calclab.emite.core.client.events;

import com.calclab.emite.core.client.packet.IPacket;
import com.google.gwt.event.shared.GwtEvent;

public abstract class PacketEvent extends GwtEvent<PacketHandler> {
	private final Type<PacketHandler> type;
	private final IPacket packet;

	public PacketEvent(final Type<PacketHandler> type, final IPacket packet) {
		this.type = type;
		this.packet = packet;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<PacketHandler> getAssociatedType() {
		return type;
	}

	public IPacket getPacket() {
		return packet;
	}

	@Override
	protected void dispatch(final PacketHandler handler) {
		handler.onPacket(this);
	}

}
