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

package com.calclab.emite.core.session;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;

import com.calclab.emite.core.IQCallback;
import com.calclab.emite.core.events.IQResponseReceivedEvent;
import com.calclab.emite.core.stanzas.IQ;
import com.google.common.collect.Maps;

/**
 * Handles IQ responses.
 */
final class IQManager implements IQResponseReceivedEvent.Handler {

	private final XmppSessionImpl session;
	private final Map<String, IQCallback> iqHandlers;
	private int iqId;
	
	protected IQManager(final XmppSessionImpl session) {
		this.session = checkNotNull(session);
		iqHandlers = Maps.newHashMap();

		session.addIQResponseReceivedHandler(this);
	}

	@Override
	public void onIQResponseReceived(final IQResponseReceivedEvent event) {
		final IQ iq = event.getIQ();
		
		final IQCallback handler = iqHandlers.remove(iq.getId());
		if (handler == null)
			return;

		if (IQ.Type.result.equals(iq.getType())) {
			handler.onIQSuccess(iq);
		} else {
			handler.onIQFailure(iq);
		}
	}
	
	protected final void sendIQRequest(final String category, final IQ iq, final IQCallback handler, final boolean force) {
		if (!IQ.Type.result.equals(iq.getType())) {
			final String key = category + "_" + iqId++;
			iqHandlers.put(key, handler);
			iq.setId(key);
			session.send(iq, force);
		}
	}
	
}
