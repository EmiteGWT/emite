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

package com.calclab.emite.core;

import com.calclab.emite.base.LoggingEventBus;
import com.calclab.emite.core.conn.XmppConnection;
import com.calclab.emite.core.conn.XmppConnectionBosh;
import com.calclab.emite.core.session.XmppSession;
import com.calclab.emite.core.session.XmppSessionImpl;
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

		bind(XmppConnection.class).to(XmppConnectionBosh.class);
		bind(XmppSession.class).to(XmppSessionImpl.class);
	}

}
