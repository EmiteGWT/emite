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

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.MatcherFactory;
import com.calclab.emite.core.client.packet.NoPacket;
import com.calclab.emite.core.client.packet.PacketMatcher;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * An error has occurred. If is an error from an incoming stanza, the getStanza
 * and getErrorStanza methods returns something different from NoPacket.INSTANCE
 */
public class ErrorEvent extends GwtEvent<ErrorHandler> {

	private static final PacketMatcher ERROR_STANZA_MATCHER = MatcherFactory.byName("error");
	private static final Type<ErrorHandler> TYPE = new Type<ErrorHandler>();

	public static HandlerRegistration bind(final EmiteEventBus eventBus, final ErrorHandler handler) {
		return eventBus.addHandler(TYPE, handler);
	}

	private final String errorType;
	private final String description;
	private final IPacket stanza;
	private final IPacket errorStanza;

	public ErrorEvent(final String errorType, final String description, final IPacket stanza) {
		this.errorType = errorType;
		this.description = description;
		this.stanza = stanza != null ? stanza : NoPacket.INSTANCE;
		errorStanza = this.stanza.getFirstChild(ERROR_STANZA_MATCHER);
	}

	@Override
	public Type<ErrorHandler> getAssociatedType() {
		return TYPE;
	}

	public String getDescription() {
		return description;
	}

	/**
	 * The associated error stanza if any
	 * 
	 * @return the error stanza of NoPacket.INSTANCE if none
	 * @see http://xmpp.org/rfcs/rfc3920.html#rfc.section.4.7
	 */
	public IPacket getErrorStanza() {
		return errorStanza;
	}

	public String getErrorType() {
		return errorType;
	}

	/**
	 * The server side stanza that fired the error event
	 * 
	 * @return never null: NoPacket.INSTANCE if the is a client side error
	 */
	public IPacket getStanza() {
		return stanza;
	}

	@Override
	protected void dispatch(final ErrorHandler handler) {
		handler.onError(this);
	}

}
