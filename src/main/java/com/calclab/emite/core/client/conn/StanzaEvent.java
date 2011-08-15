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

package com.calclab.emite.core.client.conn;

import com.calclab.emite.core.client.packet.IPacket;
import com.google.gwt.event.shared.GwtEvent;

public class StanzaEvent extends GwtEvent<StanzaHandler> {

	private final Type<StanzaHandler> type;
	private final IPacket stanza;

	public StanzaEvent(final Type<StanzaHandler> type, final IPacket stanza) {
		this.type = type;
		this.stanza = stanza;
	}

	@Override
	protected void dispatch(final StanzaHandler handler) {
		handler.onStanza(this);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<StanzaHandler> getAssociatedType() {
		return type;
	}

	public IPacket getStanza() {
		return stanza;
	}

	@Override
	public String toDebugString() {
		return super.toDebugString() + stanza;
	}

}
