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

package com.calclab.emite.im.client.chat.events;

import com.calclab.emite.core.client.events.ChangedEvent;
import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.im.client.chat.Chat;
import com.google.gwt.event.shared.HandlerRegistration;

public class ChatChangedEvent extends ChangedEvent<ChatChangedHandler> {

	private static final Type<ChatChangedHandler> TYPE = new Type<ChatChangedHandler>();

	public static HandlerRegistration bind(final EmiteEventBus eventBus, final ChatChangedHandler handler) {
		return eventBus.addHandler(TYPE, handler);
	}

	private final Chat chat;

	public ChatChangedEvent(final String changeType, final Chat chat) {
		super(TYPE, changeType);
		assert chat != null : "Chat can't be null in ChatChangedEvent";
		this.chat = chat;
	}

	public Chat getChat() {
		return chat;
	}

	@Override
	public String toDebugString() {
		return super.toDebugString() + chat.getURI();
	}

	@Override
	protected void dispatch(final ChatChangedHandler handler) {
		handler.onChatChanged(this);
	}

}
