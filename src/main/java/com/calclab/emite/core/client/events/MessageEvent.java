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

import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.google.gwt.event.shared.GwtEvent;

public abstract class MessageEvent extends GwtEvent<MessageHandler> {
	private final Message message;
	private final Type<MessageHandler> type;

	public MessageEvent(final Type<MessageHandler> type, final Message message) {
		this.type = type;
		this.message = message;
	}

	@Override
	protected void dispatch(final MessageHandler handler) {
		handler.onMessage(this);
	}

	@Override
	public Type<MessageHandler> getAssociatedType() {
		return type;
	}

	public Message getMessage() {
		return message;
	}

	@Override
	public String toDebugString() {
		return super.toDebugString() + message;
	}

}
