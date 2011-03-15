package com.calclab.emite.xep.chatstate.client;

import com.calclab.emite.core.client.xmpp.session.SessionComponentsRegistry;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Register ChatStateManager as Session component
 * 
 * @see SessionComponentsRegistry
 */
public class ChatStateComponents {
    @Inject
    public ChatStateComponents(SessionComponentsRegistry registry, Provider<StateManager> provider) {
	registry.addProvider(provider);
    }
}
