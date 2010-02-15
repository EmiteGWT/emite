/*
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008-2010 The emite development team (see CREDITS for details)
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
package com.calclab.emite.xep.mucchatstate.client;

import com.calclab.emite.core.client.xmpp.session.SessionComponent;
import com.calclab.emite.xep.muc.client.RoomManager;
import com.calclab.suco.client.Suco;
import com.calclab.suco.client.ioc.module.AbstractModule;
import com.calclab.suco.client.ioc.module.Factory;
import com.google.gwt.core.client.EntryPoint;

public class MUCChatStateModule extends AbstractModule implements EntryPoint {
    public MUCChatStateModule() {
	super();
    }

    @Override
    public void onInstall() {
	register(SessionComponent.class, new Factory<MUCChatStateManager>(MUCChatStateManager.class) {
	    @Override
	    public MUCChatStateManager create() {
		return new MUCChatStateManager($(RoomManager.class));
	    }
	});
    }

    public void onModuleLoad() {
	Suco.install(this);
    }
}