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

public abstract class IQEvent extends GwtEvent<IQHandler> {

	private final IQ iq;
	private final Type<IQHandler> type;

	public IQEvent(final Type<IQHandler> type, final IQ iq) {
		this.type = type;
		this.iq = iq;
	}

	@Override
	public Type<IQHandler> getAssociatedType() {
		return type;
	}

	public IQ getIQ() {
		return iq;
	}

	@Override
	public String toDebugString() {
		return super.toDebugString() + iq;
	}

	@Override
	protected void dispatch(final IQHandler handler) {
		handler.onPacket(this);
	}

}
