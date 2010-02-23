/*
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008-2009 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.calclab.emite.im.client;

import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.session.SessionComponent;
import com.calclab.emite.core.client.xmpp.session.SessionReady;
import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.emite.im.client.chat.PairChatManager;
import com.calclab.emite.im.client.presence.PresenceManager;
import com.calclab.emite.im.client.presence.PresenceManagerImpl;
import com.calclab.emite.im.client.roster.Roster;
import com.calclab.emite.im.client.roster.XmppRoster;
import com.calclab.emite.im.client.roster.SubscriptionHandler;
import com.calclab.emite.im.client.roster.SubscriptionManager;
import com.calclab.emite.im.client.roster.SubscriptionManagerImpl;
import com.calclab.suco.client.Suco;
import com.calclab.suco.client.ioc.decorator.Singleton;
import com.calclab.suco.client.ioc.module.AbstractModule;
import com.calclab.suco.client.ioc.module.Factory;
import com.google.gwt.core.client.EntryPoint;

/**
 * <p>
 * Implementation of the RFC-3921
 * </p>
 * <p>
 * This module exports the following components:
 * </p>
 * <ul>
 * <li>ChatManager: A facade that simplifies one to one chats</li>
 * <li>RosterManager: represents the user's roster. Can add, remove and update
 * roster items.</li>
 * <li>SubscriptionManager: handle all the presence subscription uses.
 * Automatically subscribes the presence of the roster items, so users usually
 * doesnt need to interact with this component directly.
 * <li>PresenceManager: handles user presence and takes care about initial
 * presence</li>
 * </ul>
 * 
 * @see http://www.xmpp.org/rfcs/rfc3921.html
 */
public class InstantMessagingModule extends AbstractModule implements EntryPoint {

    public InstantMessagingModule() {
	super();
    }

    @Override
    public void onInstall() {
	container.removeProvider(SessionReady.class);

	register(SessionComponent.class, new Factory<Roster>(Roster.class) {
	    @Override
	    public Roster create() {
		return new XmppRoster($(Session.class));
	    }
	}, new Factory<ChatManager>(ChatManager.class) {
	    @Override
	    public PairChatManager create() {
		return new PairChatManager($(Session.class));
	    }
	}, new Factory<SubscriptionManager>(SubscriptionManager.class) {
	    @Override
	    public SubscriptionManager create() {
		return new SubscriptionManagerImpl($(Session.class), $(Roster.class));
	    }
	}, new Factory<PresenceManager>(PresenceManager.class) {
	    @Override
	    public PresenceManager create() {
		return new PresenceManagerImpl($(Session.class), $(Roster.class));
	    }
	});

	register(Singleton.class, new Factory<SubscriptionHandler>(SubscriptionHandler.class) {
	    @Override
	    public SubscriptionHandler create() {
		return new SubscriptionHandler($(SubscriptionManager.class));
	    }
	});

    }

    public void onModuleLoad() {
	Suco.install(this);
    }
}
