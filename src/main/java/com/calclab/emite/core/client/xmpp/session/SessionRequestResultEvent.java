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

package com.calclab.emite.core.client.xmpp.session;

import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class SessionRequestResultEvent extends GwtEvent<SessionRequestResultHandler> {

	private static final Type<SessionRequestResultHandler> TYPE = new Type<SessionRequestResultHandler>();

	public static HandlerRegistration bind(final EmiteEventBus eventBus, final SessionRequestResultHandler handler) {
		return eventBus.addHandler(TYPE, handler);
	}

	public static Type<SessionRequestResultHandler> getType() {
		return TYPE;
	}

	private final boolean succeed;

	private final XmppURI uri;

	public SessionRequestResultEvent(final XmppURI uri) {
		this(true, uri);
	}

	private SessionRequestResultEvent(final boolean succeed, final XmppURI uri) {
		this.succeed = succeed;
		this.uri = uri;
	}

	@Override
	public Type<SessionRequestResultHandler> getAssociatedType() {
		return TYPE;
	}

	public XmppURI getXmppUri() {
		return uri;
	}

	public boolean isSucceed() {
		return succeed;
	}

	@Override
	protected void dispatch(final SessionRequestResultHandler handler) {
		handler.onSessionRequestResult(this);
	}

}
