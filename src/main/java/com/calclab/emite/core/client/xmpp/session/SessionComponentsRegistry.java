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

package com.calclab.emite.core.client.xmpp.session;

import java.util.HashSet;
import java.util.logging.Logger;

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

	private static final Logger logger = Logger.getLogger(SessionComponentsRegistry.class.getName());
	
	private final HashSet<Provider<?>> providers;
	private boolean componentsCreated;

	@Inject
	public SessionComponentsRegistry() {
		providers = new HashSet<Provider<?>>();
		componentsCreated = false;
	}

	public void addProvider(final Provider<?> provider) {
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
		logger.finer("SessionComponentsRegistry - Create components");
		assert componentsCreated == false : "Session only can be started once!";
		for (final Provider<?> provider : providers) {
			provider.get();
		}
		providers.clear();
		componentsCreated = true;
	}
}
