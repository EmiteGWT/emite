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
package com.calclab.emite.xep.avatar.client;

import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.suco.client.Suco;
import com.calclab.suco.client.ioc.decorator.Singleton;
import com.calclab.suco.client.ioc.module.AbstractModule;
import com.calclab.suco.client.ioc.module.Factory;
import com.google.gwt.core.client.EntryPoint;

// FIXME - doc
/**
 * AvatarModule: implements XEP-FIXME
 * <ul>
 * <li>AvatarManager</li>
 * </ul>
 * 
 * 
 * @see FIXME
 */
public class AvatarModule extends AbstractModule implements EntryPoint {

    public AvatarModule() {
	super();
    }

    @Override
    public void onInstall() {
	register(Singleton.class, new Factory<AvatarManager>(AvatarManager.class) {
	    @Override
	    public AvatarManager create() {
		return new AvatarManager($(Session.class));
	    }
	});
    }

    public void onModuleLoad() {
	Suco.install(this);
    }
}
