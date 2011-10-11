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

import com.calclab.emite.core.client.stanzas.Stanza;
import com.calclab.emite.core.client.xml.XMLPacket;
import com.google.web.bindery.event.shared.Event;

/**
 * An error has occurred. If is an error from an incoming stanza, the getStanza
 * and getErrorStanza methods returns something different from NoPacket.INSTANCE
 */
public class ErrorEvent extends Event<ErrorEvent.Handler> {

	public interface Handler {
		void onError(ErrorEvent event);
	}

	public static final Type<Handler> TYPE = new Type<Handler>();

	private final String errorType;
	private final String description;
	private final Stanza stanza;
	private final XMLPacket error;

	public ErrorEvent(final String errorType, final String description, final Stanza stanza) {
		this.errorType = errorType;
		this.description = description;
		this.stanza = stanza;
		error = stanza != null ? stanza.getXML().getFirstChild("error") : null;
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
	public XMLPacket getError() {
		return error;
	}

	public String getErrorType() {
		return errorType;
	}

	/**
	 * The server side stanza that fired the error event
	 * 
	 * @return never null: NoPacket.INSTANCE if the is a client side error
	 */
	public Stanza getStanza() {
		return stanza;
	}

	@Override
	public Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(final Handler handler) {
		handler.onError(this);
	}

}
