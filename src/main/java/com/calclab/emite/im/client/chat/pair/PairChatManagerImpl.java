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

package com.calclab.emite.im.client.chat.pair;

import com.calclab.emite.core.client.events.ChangedEvent.ChangeType;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.calclab.emite.im.client.chat.ChatManagerBoilerplate;
import com.calclab.emite.im.client.chat.ChatProperties;
import com.calclab.emite.im.client.chat.ChatSelectionStrategy;
import com.calclab.emite.im.client.chat.ChatStates;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;

@Singleton
public class PairChatManagerImpl extends ChatManagerBoilerplate<PairChat> implements PairChatManager {

	@Inject
	public PairChatManagerImpl(@Named("emite") final EventBus eventBus, final XmppSession session, @Named("Pair") final ChatSelectionStrategy strategy) {
		super(eventBus, session, strategy);
	}
	
	@Override
	protected void fireChanged(ChangeType type, PairChat chat) {
		eventBus.fireEventFromSource(new PairChatChangedEvent(type, chat), this);
	}
	
	@Override
	public HandlerRegistration addPairChatChangedHandler(final PairChatChangedEvent.Handler handler) {
		return eventBus.addHandlerToSource(PairChatChangedEvent.TYPE, this, handler);
	}

	@Override
	protected PairChat createChat(final ChatProperties properties) {
		if (properties.getState() == null) {
			properties.setState(session.isReady() ? ChatStates.ready : ChatStates.locked);
		}
		return new PairChat(eventBus, session, properties);
	}

}
