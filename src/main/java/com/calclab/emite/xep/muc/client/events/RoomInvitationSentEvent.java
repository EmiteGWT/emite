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
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class RoomInvitationSentEvent extends GwtEvent<RoomInvitationSentHandler> {

	private static final Type<RoomInvitationSentHandler> TYPE = new Type<RoomInvitationSentHandler>();

	public static HandlerRegistration bind(final EmiteEventBus eventBus, final RoomInvitationSentHandler handler) {
		return eventBus.addHandler(TYPE, handler);
	}

	private final XmppURI userJid;

	private final String reasonText;

	public RoomInvitationSentEvent(final XmppURI userJid, final String reasonText) {
		this.userJid = userJid;
		this.reasonText = reasonText;
	}

	@Override
	public Type<RoomInvitationSentHandler> getAssociatedType() {
		return TYPE;
	}

	public String getReasonText() {
		return reasonText;
	}

	public XmppURI getUserJid() {
		return userJid;
	}

	@Override
	protected void dispatch(final RoomInvitationSentHandler handler) {
		handler.onRoomInvitationSent(this);
	}

}
