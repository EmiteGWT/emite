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

package com.calclab.emite.core.client;

import com.calclab.emite.core.client.browser.AutoConfigBoot;
import com.calclab.emite.core.client.conn.XmppConnection;
import com.calclab.emite.core.client.conn.bosh.XmppBoshConnection;
import com.calclab.emite.core.client.session.SessionReady;
import com.calclab.emite.core.client.session.XmppSession;
import com.calclab.emite.core.client.session.XmppSessionImpl;
import com.calclab.emite.core.client.session.sasl.SASLManager;
import com.calclab.emite.core.client.session.sasl.SASLManagerImpl;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import com.google.web.bindery.event.shared.EventBus;

/**
 * Emite Core GIN module
 */
public class EmiteCoreModule extends AbstractGinModule {

	@Override
	protected void configure() {
		bind(EventBus.class).annotatedWith(Names.named("emite")).to(LoggingEventBus.class).in(Singleton.class);

		bind(XmppConnection.class).to(XmppBoshConnection.class);
		bind(XmppSession.class).to(XmppSessionImpl.class);
		bind(SASLManager.class).to(SASLManagerImpl.class);

		bind(SessionReady.class).asEagerSingleton();
		bind(AutoConfigBoot.class).asEagerSingleton();
	}

}
