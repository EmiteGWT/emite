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

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.xep.disco.client.DiscoveryItemsResults;
import com.google.gwt.event.shared.GwtEvent;

/**
 * A discovery items result event. Can be a not successful event
 * 
 */
public class DiscoveryItemsResultEvent extends GwtEvent<DiscoveryItemsResultHandler> {

	private static final Type<DiscoveryItemsResultHandler> TYPE = new Type<DiscoveryItemsResultHandler>();
	private final DiscoveryItemsResults itemsResult;
	private final IPacket errorPacket;

	public DiscoveryItemsResultEvent(final DiscoveryItemsResults infoResult) {
		this(infoResult, null);
	}

	private DiscoveryItemsResultEvent(final DiscoveryItemsResults itemsResult, final IPacket errorPacket) {
		assert itemsResult != null && errorPacket == null || itemsResult == null && errorPacket != null : "Discovery event only can have or result or error";
		this.itemsResult = itemsResult;
		this.errorPacket = errorPacket;
	}

	public DiscoveryItemsResultEvent(final IPacket errorPacket) {
		this(null, errorPacket);
	}

	@Override
	protected void dispatch(final DiscoveryItemsResultHandler handler) {
		handler.onDiscoveryItemsResult(this);
	}

	@Override
	public GwtEvent.Type<DiscoveryItemsResultHandler> getAssociatedType() {
		return TYPE;
	}

	public IPacket getErrorPacket() {
		return errorPacket;
	}

	public DiscoveryItemsResults getResults() {
		return itemsResult;
	}

	public boolean hasResult() {
		return itemsResult != null;
	}

}
