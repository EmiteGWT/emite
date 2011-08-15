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

package com.calclab.emite.im.client.roster.events;

import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class SubscriptionRequestReceivedEvent extends GwtEvent<SubscriptionRequestReceivedHandler> {

	private static final Type<SubscriptionRequestReceivedHandler> TYPE = new Type<SubscriptionRequestReceivedHandler>();

	public static HandlerRegistration bind(final EmiteEventBus eventBus, final SubscriptionRequestReceivedHandler handler) {
		return eventBus.addHandler(TYPE, handler);
	}

	private final String nick;

	private final XmppURI from;

	public SubscriptionRequestReceivedEvent(final XmppURI from, final String nick) {
		this.from = from;
		this.nick = nick;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<SubscriptionRequestReceivedHandler> getAssociatedType() {
		return TYPE;
	}

	public XmppURI getFrom() {
		return from;
	}

	public String getNick() {
		return nick;
	}

	@Override
	protected void dispatch(final SubscriptionRequestReceivedHandler handler) {
		handler.onSubscriptionRequestReceived(this);
	}

}
