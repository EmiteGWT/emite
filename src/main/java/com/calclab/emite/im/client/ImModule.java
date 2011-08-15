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

package com.calclab.emite.im.client;

import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.emite.im.client.chat.ChatSelectionStrategy;
import com.calclab.emite.im.client.chat.PairChatManager;
import com.calclab.emite.im.client.chat.PairChatSelectionStrategy;
import com.calclab.emite.im.client.presence.PresenceManager;
import com.calclab.emite.im.client.presence.PresenceManagerImpl;
import com.calclab.emite.im.client.roster.SubscriptionHandler;
import com.calclab.emite.im.client.roster.SubscriptionManager;
import com.calclab.emite.im.client.roster.SubscriptionManagerImpl;
import com.calclab.emite.im.client.roster.XmppRoster;
import com.calclab.emite.im.client.roster.XmppRosterLogic;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;
import com.google.inject.name.Names;

/**
 * Implementation of the RFC-3921 (Extensible Messaging and Presence Protocol
 * (XMPP): Instant Messaging and Presence)
 * 
 * This module implements the extensions to and applications of the core
 * features of XMPP that provide the basic functionality expected of an instant
 * messaging (IM) and presence application.
 * 
 * @see http://www.xmpp.org/rfcs/rfc3921.html
 */
public class ImModule extends AbstractGinModule {
	@Override
	protected void configure() {
		bind(ChatManager.class).to(PairChatManager.class).in(Singleton.class);
		bind(PresenceManager.class).to(PresenceManagerImpl.class).in(Singleton.class);
		bind(SubscriptionManager.class).to(SubscriptionManagerImpl.class).in(Singleton.class);
		bind(SubscriptionHandler.class).in(Singleton.class);
		bind(XmppRoster.class).to(XmppRosterLogic.class).in(Singleton.class);
		bind(ImComponents.class).asEagerSingleton();
		bind(ChatSelectionStrategy.class).annotatedWith(Names.named("Pair")).to(PairChatSelectionStrategy.class).in(Singleton.class);
	}

}
