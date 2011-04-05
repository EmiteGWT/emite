package com.calclab.emite.im.client;

import com.calclab.emite.core.client.xmpp.session.SessionComponentsRegistry;
import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.emite.im.client.presence.PresenceManager;
import com.calclab.emite.im.client.roster.Roster;
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
public class ImComponents {

    @Inject
    public ImComponents(SessionComponentsRegistry registry, Provider<ChatManager> chatManager,
	    Provider<PresenceManager> presenceManager, Provider<Roster> roster,
	    Provider<SubscriptionManager> subscriptionManager, Provider<SubscriptionHandler> subcriptionHandler,
	    Provider<XmppRoster> xmppRoster) {

	registry.addProvider(chatManager);
	registry.addProvider(presenceManager);
	registry.addProvider(roster);
	registry.addProvider(subscriptionManager);
	registry.addProvider(subcriptionHandler);
	registry.addProvider(xmppRoster);
    }
}
