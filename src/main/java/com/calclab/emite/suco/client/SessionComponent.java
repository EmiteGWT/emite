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
package com.calclab.emite.suco.client;

import com.calclab.suco.client.ioc.Container;
import com.calclab.suco.client.ioc.Provider;
import com.calclab.suco.client.ioc.decorator.ProviderCollection;
import com.calclab.suco.client.ioc.decorator.Singleton;
import com.google.gwt.core.client.GWT;

/**
 * All the providers/factories decorated by SessionComponent are called when a
 * new Session component is created.
 * 
 * Use this decorator if your component DEPENDS on Session to perform its tasks
 */
public class SessionComponent extends ProviderCollection {
    private boolean initialized;

    public SessionComponent(final Container container) {
	super(container, Singleton.instance);
	initialized = false;
    }

    @Override
    public <T> Provider<T> decorate(final Class<T> type, final Provider<T> undecorated) {
	if (initialized == true) {
	    undecorated.get();
	}
	return super.decorate(type, undecorated);
    }

    public void init() {
	if (initialized == false) {
	    GWT.log("SESSION COMPONENTS!", null);
	    for (final Provider<?> p : getProviders()) {
		p.get();
	    }
	    initialized = true;
	}
    }
}
