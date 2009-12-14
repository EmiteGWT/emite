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
package com.calclab.emite.xep.chatstate.client;

import com.calclab.emite.core.client.xmpp.session.SessionComponent;
import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.suco.client.Suco;
import com.calclab.suco.client.ioc.module.AbstractModule;
import com.calclab.suco.client.ioc.module.Factory;
import com.google.gwt.core.client.EntryPoint;

/**
 * Implements XEP-0085: Chat State Notifications
 * 
 * @see http://www.xmpp.org/extensions/xep-0085.html (Version: 1.2)
 * 
 */
public class ChatStateModule extends AbstractModule implements EntryPoint {
    public ChatStateModule() {
	super();
    }

    @Override
    public void onInstall() {
	register(SessionComponent.class, new Factory<StateManager>(StateManager.class) {
	    @Override
	    public StateManager create() {
		return new StateManager($(ChatManager.class));
	    }
	});
    }

    public void onModuleLoad() {
	Suco.install(this);
    }
}
