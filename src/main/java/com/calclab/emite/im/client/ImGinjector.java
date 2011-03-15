package com.calclab.emite.im.client;

import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.emite.im.client.presence.PresenceManager;
import com.calclab.emite.im.client.roster.Roster;
import com.calclab.emite.im.client.roster.SubscriptionHandler;
import com.calclab.emite.im.client.roster.SubscriptionManager;
import com.calclab.emite.im.client.roster.XmppRoster;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules(ImModule.class)
public interface ImGinjector extends Ginjector {
    ChatManager getChatManager();

    PresenceManager getPresenceManager();

    Roster getRoster();

    SubscriptionHandler getSubscriptionHandler();

    SubscriptionManager getSubscriptionManager();

    XmppRoster getXmppRoster();
}
