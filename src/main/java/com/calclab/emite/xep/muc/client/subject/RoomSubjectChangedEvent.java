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

package com.calclab.emite.xep.muc.client.subject;

import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * An event to inform about room subject changes
 * 
 * @author dani
 * 
 */
public class RoomSubjectChangedEvent extends GwtEvent<RoomSubjectChangedHandler> {

	private static final Type<RoomSubjectChangedHandler> TYPE = new Type<RoomSubjectChangedHandler>();

	public static HandlerRegistration bind(final EmiteEventBus eventBus, final RoomSubjectChangedHandler handler) {
		return eventBus.addHandler(TYPE, handler);
	}

	private final String subject;
	private final XmppURI occupantUri;

	public RoomSubjectChangedEvent(final XmppURI occupantUri, final String subject) {
		this.occupantUri = occupantUri;
		this.subject = subject;
	}

	@Override
	public Type<RoomSubjectChangedHandler> getAssociatedType() {
		return TYPE;
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
	protected void dispatch(final RoomSubjectChangedHandler handler) {
		handler.onSubjectChanged(this);
	}

}
