package com.calclab.emite.xep.mucchatstate.client;

import com.calclab.emite.core.client.xmpp.session.SessionComponentsRegistry;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Add MucChatStateManager as session component
 * 
 * @see SessionComponentsRegistry
 * 
 */
public class MucChatStateComponents {

    @Inject
    public MucChatStateComponents(SessionComponentsRegistry registry, Provider<MUCChatStateManager> provider) {
	registry.addProvider(provider);
    }
}
