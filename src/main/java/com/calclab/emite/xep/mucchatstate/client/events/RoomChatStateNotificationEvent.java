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

package com.calclab.emite.xep.mucchatstate.client.events;

import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.xep.chatstate.client.ChatStateManager.ChatState;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class RoomChatStateNotificationEvent extends GwtEvent<RoomChatStateNotificationHandler> {

	private static final Type<RoomChatStateNotificationHandler> TYPE = new Type<RoomChatStateNotificationHandler>();

	public static HandlerRegistration bind(final EmiteEventBus eventBus, final RoomChatStateNotificationHandler handler) {
		return eventBus.addHandler(TYPE, handler);
	}

	private final XmppURI from;

	private final ChatState chatState;

	public RoomChatStateNotificationEvent(final XmppURI from, final ChatState chatState) {
		this.from = from;
		this.chatState = chatState;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<RoomChatStateNotificationHandler> getAssociatedType() {
		return TYPE;
	}

	public ChatState getChatState() {
		return chatState;
	}

	public XmppURI getFrom() {
		return from;
	}

	@Override
	protected void dispatch(final RoomChatStateNotificationHandler handler) {
		handler.onRoomChatStateNotification(this);
	}

}
