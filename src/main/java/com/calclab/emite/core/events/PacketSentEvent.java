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

package com.calclab.emite.core.events;

import static com.google.common.base.Preconditions.checkNotNull;

import com.calclab.emite.base.xml.XMLPacket;
import com.google.web.bindery.event.shared.Event;

public class PacketSentEvent extends Event<PacketSentEvent.Handler> {

	public interface Handler {
		void onStanzaSent(PacketSentEvent event);
	}

	public static final Type<Handler> TYPE = new Type<Handler>();

	private final XMLPacket packet;

	public PacketSentEvent(final XMLPacket packet) {
		this.packet = checkNotNull(packet);
	}

	public XMLPacket getPacket() {
		return packet;
	}

	@Override
	public Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(final Handler handler) {
		handler.onStanzaSent(this);
	}

	@Override
	public String toDebugString() {
		return super.toDebugString() + packet;
	}

}
