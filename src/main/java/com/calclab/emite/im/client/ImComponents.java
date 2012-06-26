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

import java.util.HashMap;

import com.calclab.emite.core.client.LoginXmpp;
import com.calclab.emite.core.client.LoginXmppMap;
import com.calclab.emite.core.client.MultiInstance;
import com.calclab.emite.core.client.xmpp.session.SessionComponentsRegistry;
import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.emite.im.client.presence.PresenceManager;
import com.calclab.emite.im.client.roster.SubscriptionHandler;
import com.calclab.emite.im.client.roster.SubscriptionManager;
import com.calclab.emite.im.client.roster.XmppRoster;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

/**
 * Register the IM session components. When a new XmppSession is created, the
 * given providers are called
 * 
 * @see SessionComponentsRegistry
 */
@Singleton
public class ImComponents implements MultiInstance {
	Provider<ChatManager> chatManager;
    Provider<PresenceManager> presenceManager;
    Provider<SubscriptionManager> subscriptionManager;
    Provider<SubscriptionHandler> subcriptionHandler;
    Provider<XmppRoster> xmppRoster;
	HashMap <String, LoginXmpp>loginXmppMap;
	private LoginXmpp loginXmpp;

	@Inject
	public ImComponents(final SessionComponentsRegistry registry, final Provider<ChatManager> chatManager, final Provider<PresenceManager> presenceManager,
			final Provider<SubscriptionManager> subscriptionManager, final Provider<SubscriptionHandler> subcriptionHandler,
			final Provider<XmppRoster> xmppRoster, @LoginXmppMap  HashMap <String, LoginXmpp> loginXmppMap, LoginXmpp loginXmpp) {

    	this.chatManager = chatManager;
		this.presenceManager = presenceManager;
		this.subscriptionManager = subscriptionManager;
		this.subcriptionHandler = subcriptionHandler;
		this.xmppRoster = xmppRoster; 
		this.loginXmppMap = loginXmppMap;
		this.loginXmpp = loginXmpp;
/*
		registry.addProvider(chatManager);
		registry.addProvider(presenceManager);
		registry.addProvider(subscriptionManager);
		registry.addProvider(subcriptionHandler);
		registry.addProvider(xmppRoster);
*/
	}
	@Override
	public void setInstanceId(String instanceId) {
		loginXmppMap.put(instanceId, loginXmpp);
		loginXmpp.setInstanceId(instanceId);
    	
    	loginXmpp.registry.addProvider(chatManager);
    	loginXmpp.registry.addProvider(presenceManager);
    	loginXmpp.registry.addProvider(subscriptionManager);
    	loginXmpp.registry.addProvider(subcriptionHandler);
    	loginXmpp.registry.addProvider(xmppRoster);
		
	}
}
