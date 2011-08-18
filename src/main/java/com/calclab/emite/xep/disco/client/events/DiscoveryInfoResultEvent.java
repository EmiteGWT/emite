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

package com.calclab.emite.xep.disco.client.events;

import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.xep.disco.client.DiscoveryInfoResults;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class DiscoveryInfoResultEvent extends GwtEvent<DiscoveryInfoResultHandler> {

	private static final Type<DiscoveryInfoResultHandler> TYPE = new Type<DiscoveryInfoResultHandler>();

	public static HandlerRegistration bind(final EmiteEventBus eventBus, final DiscoveryInfoResultHandler handler) {
		return eventBus.addHandler(TYPE, handler);
	}

	private final DiscoveryInfoResults infoResult;
	private final IPacket errorPacket;

	public DiscoveryInfoResultEvent(final DiscoveryInfoResults infoResult) {
		this(infoResult, null);
	}

	public DiscoveryInfoResultEvent(final IPacket errorPacket) {
		this(null, errorPacket);
	}

	private DiscoveryInfoResultEvent(final DiscoveryInfoResults infoResult, final IPacket errorPacket) {
		assert !(infoResult != null && errorPacket != null) : "Discovery event can have either or result or error, not both";
		assert !(infoResult == null && errorPacket == null) : "Discovery event must have or result or error";
		this.infoResult = infoResult;
		this.errorPacket = errorPacket;
	}

	@Override
	public Type<DiscoveryInfoResultHandler> getAssociatedType() {
		return TYPE;
	}

	public IPacket getErrorPacket() {
		return errorPacket;
	}

	public DiscoveryInfoResults getResults() {
		return infoResult;
	}

	public boolean hasResult() {
		return infoResult != null;
	}

	@Override
	protected void dispatch(final DiscoveryInfoResultHandler handler) {
		handler.onDiscoveryInfoResult(this);
	}

}
