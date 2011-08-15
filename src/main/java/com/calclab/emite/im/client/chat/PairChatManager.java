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

package com.calclab.emite.im.client.chat;

import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

/**
 * Default ChatManager implementation. Use ChatManager interface instead
 * 
 * @see ChatManager
 */
@Singleton
public class PairChatManager extends AbstractChatManager {

	public PairChatManager(final XmppSession session) {
		this(session, new PairChatSelectionStrategy());
	}

	@Inject
	public PairChatManager(final XmppSession session, @Named("Pair") final ChatSelectionStrategy strategy) {
		super(session, strategy);
	}

	@Override
	protected Chat createChat(final ChatProperties properties) {
		if (properties.getState() == null) {
			properties.setState(session.isReady() ? ChatStates.ready : ChatStates.locked);
		}
		return new PairChat(session, properties);
	}

}
