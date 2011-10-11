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

package com.calclab.emite.xep.muc.client;

import com.calclab.emite.core.client.stanzas.XmppURI;
import com.google.web.bindery.event.shared.Event;

/**
 * An event to inform about room subject changes
 * 
 * @author dani
 * 
 */
public class RoomSubjectChangedEvent extends Event<RoomSubjectChangedEvent.Handler> {

	public interface Handler {
		void onRoomSubjectChanged(RoomSubjectChangedEvent event);
	}

	public static final Type<Handler> TYPE = new Type<Handler>();

	private final XmppURI occupantUri;
	private final String subject;

	public RoomSubjectChangedEvent(final XmppURI occupantUri, final String subject) {
		this.occupantUri = occupantUri;
		this.subject = subject;
	}

	/**
	 * Get modificator's occupant (room and nick) uri
	 * 
	 * @return
	 */
	public XmppURI getOccupantUri() {
		return occupantUri;
	}

	/**
	 * The new subject
	 * 
	 * @return
	 */
	public String getSubject() {
		return subject;
	}

	@Override
	public Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(final Handler handler) {
		handler.onRoomSubjectChanged(this);
	}

}
