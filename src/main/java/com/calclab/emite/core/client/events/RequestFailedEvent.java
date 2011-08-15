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

import com.calclab.emite.core.client.xmpp.stanzas.IQ;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * A request (IQ request) has failed
 */
public class RequestFailedEvent extends GwtEvent<RequestFailedHandler> {

	private static final Type<RequestFailedHandler> TYPE = new Type<RequestFailedHandler>();

	public static HandlerRegistration bind(final EmiteEventBus eventBus, final RequestFailedHandler handler) {
		return eventBus.addHandler(TYPE, handler);
	}

	private final String requestType;
	private final String description;

	private final IQ iq;

	public RequestFailedEvent(final String requestType, final String description, final IQ iq) {
		this.requestType = requestType;
		this.description = description;
		this.iq = iq;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<RequestFailedHandler> getAssociatedType() {
		return TYPE;
	}

	public String getDescription() {
		return description;
	}

	public IQ getIq() {
		return iq;
	}

	public String getRequestType() {
		return requestType;
	}

	@Override
	protected void dispatch(final RequestFailedHandler handler) {
		handler.onRequestFailed(this);
	}

}
