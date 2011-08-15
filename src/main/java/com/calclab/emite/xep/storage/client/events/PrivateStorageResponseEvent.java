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

package com.calclab.emite.xep.storage.client.events;

import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.core.client.xmpp.stanzas.IQ;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class PrivateStorageResponseEvent extends GwtEvent<PrivateStorageResponseHandler> {

	private static final Type<PrivateStorageResponseHandler> TYPE = new Type<PrivateStorageResponseHandler>();

	public static HandlerRegistration bind(final EmiteEventBus eventBus, final PrivateStorageResponseHandler handler) {
		return eventBus.addHandler(TYPE, handler);
	}

	private final IQ response;

	public PrivateStorageResponseEvent(final IQ response) {
		this.response = response;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<PrivateStorageResponseHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(final PrivateStorageResponseHandler handler) {
		handler.onStorageResponse(this);
	}

	public IQ getResponseIQ() {
		return response;
	}

}
