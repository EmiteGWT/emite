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
package com.calclab.emite.core.client;

import com.calclab.emite.core.client.bosh.BoshConnection;
import com.calclab.emite.core.client.bosh.XmppBoshConnection;
import com.calclab.emite.core.client.conn.Connection;
import com.calclab.emite.core.client.conn.XmppConnection;
import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.core.client.events.EventBusFactory;
import com.calclab.emite.core.client.services.Services;
import com.calclab.emite.core.client.services.gwt.GWTServices;
import com.calclab.emite.core.client.xmpp.resource.ResourceBindingManager;
import com.calclab.emite.core.client.xmpp.sasl.SASLManager;
import com.calclab.emite.core.client.xmpp.session.IMSessionManager;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.session.SessionComponentsRegistry;
import com.calclab.emite.core.client.xmpp.session.SessionImpl;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.calclab.emite.core.client.xmpp.session.XmppSessionLogic;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

/**
 * The Emite core module implements the Extensible Messaging and Presence
 * Protocol (XMPP): Core
 * 
 * The Extensible Messaging and Presence Protocol (XMPP) is an open Extensible
 * Markup Language XML [XML] protocol for near-real-time messaging, presence,
 * and request-response services. The basic syntax and semantics were developed
 * originally within the Jabber open-source community, mainly in 1999.
 * 
 * The core features -- mainly XML streams, use of TLS and SASL, and the
 * <message/>, <presence/>, and <iq/> children of the stream root -- provide the
 * building blocks for many types of near-real-time applications, which may be
 * layered on top of the core by sending application-specific data qualified by
 * particular XML namespaces [XML‑NAMES]
 * 
 * @see http://xmpp.org/rfcs/rfc3920.html
 */
public class CoreModule extends AbstractGinModule {

    @Override
    protected void configure() {
	bind(Services.class).to(GWTServices.class).in(Singleton.class);
	bind(XmppConnection.class).to(XmppBoshConnection.class).in(Singleton.class);
	bind(XmppSession.class).to(XmppSessionLogic.class).in(Singleton.class);
	bind(IMSessionManager.class).in(Singleton.class);
	bind(SASLManager.class).in(Singleton.class);
	bind(ResourceBindingManager.class).in(Singleton.class);
	bind(SessionComponentsRegistry.class).in(Singleton.class);
	bind(Connection.class).to(BoshConnection.class).in(Singleton.class);
	bind(Session.class).to(SessionImpl.class).in(Singleton.class);
    }

    @Provides
    @Singleton
    EmiteEventBus provideEmiteEventBus() {
	return EventBusFactory.create("emite");
    }

}
