package com.calclab.emite.xep.muc.client;

import com.calclab.emite.core.client.xmpp.session.SessionComponentsRegistry;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Register RoomManager as session component
 * 
 * @see SessionComponentsRegistry
 * 
 */
public class MucComponents {

    @Inject
    public MucComponents(SessionComponentsRegistry registry, Provider<RoomManager> provider) {
	registry.addProvider(provider);
    }
}
