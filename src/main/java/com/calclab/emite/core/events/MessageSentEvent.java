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

import com.calclab.emite.core.stanzas.Message;
import com.google.web.bindery.event.shared.Event;

public class MessageSentEvent extends Event<MessageSentEvent.Handler> {

	public interface Handler {
		void onMessageSent(MessageSentEvent event);
	}

	public static final Type<Handler> TYPE = new Type<Handler>();

	private final Message message;

	public MessageSentEvent(final Message message) {
		this.message = checkNotNull(message);
	}

	public Message getMessage() {
		return message;
	}

	@Override
	public Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(final Handler handler) {
		handler.onMessageSent(this);
	}

	@Override
	public String toDebugString() {
		return super.toDebugString() + message;
	}

}
