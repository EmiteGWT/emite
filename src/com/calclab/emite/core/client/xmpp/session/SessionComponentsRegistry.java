package com.calclab.emite.core.client.xmpp.session;

import java.util.HashSet;

import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

/**
 * A registry of session components. Session components are classes that are
 * instantiated when the XmppSession is created
 * 
 * All the providers registered are called when the XmppSession is created
 */
@Singleton
public class SessionComponentsRegistry {

    private final HashSet<Provider<?>> providers;
    private boolean componentsCreated;

    @Inject
    public SessionComponentsRegistry() {
	GWT.log("Creating SessionComponentsRegistry");

	this.providers = new HashSet<Provider<?>>();
	this.componentsCreated = false;
    }

    public void addProvider(Provider<?> provider) {
	if (componentsCreated) {
	    provider.get();
	} else {
	    providers.add(provider);
	}
    }

    public boolean areComponentsCreated() {
	return componentsCreated;
    }

    public void createComponents() {
	assert componentsCreated == false : "Session only can be started once!";
	for (Provider<?> provider : providers) {
	    provider.get();
	}
	providers.clear();
	this.componentsCreated = true;
    }
}
