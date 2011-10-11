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

package com.calclab.emite.xep.disco.client;

import com.calclab.emite.core.client.xml.XMLPacket;
import com.google.web.bindery.event.shared.Event;

public class DiscoveryInfoResultEvent extends Event<DiscoveryInfoResultEvent.Handler> {

	public interface Handler {
		void onDiscoveryInfoResult(DiscoveryInfoResultEvent event);
	}

	public static final Type<Handler> TYPE = new Type<Handler>();

	private final DiscoveryInfoResults infoResult;
	private final XMLPacket errorPacket;

	protected DiscoveryInfoResultEvent(final DiscoveryInfoResults infoResult) {
		this(infoResult, null);
	}

	protected DiscoveryInfoResultEvent(final XMLPacket errorPacket) {
		this(null, errorPacket);
	}

	private DiscoveryInfoResultEvent(final DiscoveryInfoResults infoResult, final XMLPacket errorPacket) {
		assert !(infoResult != null && errorPacket != null) : "Discovery event can have either or result or error, not both";
		assert !(infoResult == null && errorPacket == null) : "Discovery event must have or result or error";
		this.infoResult = infoResult;
		this.errorPacket = errorPacket;
	}

	@Override
	public Type<Handler> getAssociatedType() {
		return TYPE;
	}

	public XMLPacket getErrorPacket() {
		return errorPacket;
	}

	public DiscoveryInfoResults getResults() {
		return infoResult;
	}

	public boolean hasResult() {
		return infoResult != null;
	}

	@Override
	protected void dispatch(final Handler handler) {
		handler.onDiscoveryInfoResult(this);
	}

}
