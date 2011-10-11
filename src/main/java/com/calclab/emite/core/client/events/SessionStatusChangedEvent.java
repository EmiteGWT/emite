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

import com.calclab.emite.core.client.session.SessionStatus;
import com.google.web.bindery.event.shared.Event;

public class SessionStatusChangedEvent extends Event<SessionStatusChangedEvent.Handler> {

	public interface Handler {
		void onSessionStatusChanged(SessionStatusChangedEvent event);
	}

	public static final Type<Handler> TYPE = new Type<Handler>();

	private final SessionStatus status;

	public SessionStatusChangedEvent(final SessionStatus status) {
		assert status != null : "Status in SessionStatusChangedEvent can't be null";
		this.status = status;
	}

	public SessionStatus getStatus() {
		return status;
	}

	public boolean is(final SessionStatus status) {
		return this.status.equals(status);
	}

	@Override
	public Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(final Handler handler) {
		handler.onSessionStatusChanged(this);
	}

	@Override
	public String toDebugString() {
		return super.toDebugString() + " " + status;
	}

}
