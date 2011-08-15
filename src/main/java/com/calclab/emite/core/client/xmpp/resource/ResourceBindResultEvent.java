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

package com.calclab.emite.core.client.xmpp.resource;

import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class ResourceBindResultEvent extends GwtEvent<ResourceBindResultHandler> {

	private static final Type<ResourceBindResultHandler> TYPE = new Type<ResourceBindResultHandler>();

	/**
	 * A helper method to bind handlers of this event to the event bus easily
	 * 
	 * @param eventBus
	 *            the event bus to add the handler to
	 * @param handler
	 *            the handler to be added
	 * @return
	 */
	public static HandlerRegistration bind(final EmiteEventBus eventBus, final ResourceBindResultHandler handler) {
		return eventBus.addHandler(TYPE, handler);
	}

	public static Type<ResourceBindResultHandler> getType() {
		return TYPE;
	}

	private final XmppURI xmppUri;

	public ResourceBindResultEvent(final XmppURI xmppUri) {
		this.xmppUri = xmppUri;
	}

	@Override
	public Type<ResourceBindResultHandler> getAssociatedType() {
		return TYPE;
	}

	public XmppURI getXmppUri() {
		return xmppUri;
	}

	@Override
	public String toDebugString() {
		return super.toDebugString() + xmppUri;
	}

	@Override
	protected void dispatch(final ResourceBindResultHandler handler) {
		handler.onBinded(this);
	}

}
