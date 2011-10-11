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

package com.calclab.emite.xep.storage.client;

import com.calclab.emite.core.client.session.IQCallback;
import com.calclab.emite.core.client.session.XmppSession;
import com.calclab.emite.core.client.stanzas.IQ;
import com.calclab.emite.core.client.xml.HasXML;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Implements http://xmpp.org/extensions/xep-0049.html
 */
@Singleton
public class PrivateStorageManagerImpl implements PrivateStorageManager {

	private final XmppSession session;

	@Inject
	public PrivateStorageManagerImpl(final XmppSession session) {
		this.session = session;
	}

	@Override
	public void retrieve(final HasXML data, final PrivateStorageResponseEvent.Handler handler) {
		final IQ iq = new IQ(IQ.Type.get);
		iq.addChild("query", "jabber:iq:private").addChild(data);

		session.sendIQ("priv", iq, new IQCallback() {
			@Override
			public void onIQSuccess(final IQ iq) {
				handler.onStorageResponse(new PrivateStorageResponseEvent(iq));
			}

			@Override
			public void onIQFailure(final IQ iq) {
			}
		});
	}

	@Override
	public void store(final HasXML data, final PrivateStorageResponseEvent.Handler handler) {
		final IQ iq = new IQ(IQ.Type.set);
		iq.addChild("query", "jabber:iq:private").addChild(data);

		session.sendIQ("priv", iq, new IQCallback() {
			@Override
			public void onIQSuccess(final IQ iq) {
				handler.onStorageResponse(new PrivateStorageResponseEvent(iq));
			}

			@Override
			public void onIQFailure(final IQ iq) {
			}
		});
	}
}
