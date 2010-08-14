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
import com.calclab.emite.core.client.events.DefaultEmiteEventBus;
import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.core.client.services.Services;
import com.calclab.emite.core.client.services.gwt.GWTServices;
import com.calclab.emite.core.client.xmpp.datetime.XmppDateTime;
import com.calclab.emite.core.client.xmpp.resource.ResourceBindingManager;
import com.calclab.emite.core.client.xmpp.sasl.DecoderRegistry;
import com.calclab.emite.core.client.xmpp.sasl.SASLManager;
import com.calclab.emite.core.client.xmpp.session.IMSessionManager;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.session.SessionComponent;
import com.calclab.emite.core.client.xmpp.session.SessionImpl;
import com.calclab.emite.core.client.xmpp.session.SessionReady;
import com.calclab.suco.client.Suco;
import com.calclab.suco.client.ioc.decorator.Singleton;
import com.calclab.suco.client.ioc.module.AbstractModule;
import com.calclab.suco.client.ioc.module.Factory;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

public class EmiteCoreModule extends AbstractModule implements EntryPoint {

    public EmiteCoreModule() {
    }

    @Override
    public void onInstall() {
	registerDecorator(SessionComponent.class, new SessionComponent(container));

	register(Singleton.class, new Factory<Services>(Services.class) {
	    @Override
	    public Services create() {
		return new GWTServices();
	    }
	});

	register(Singleton.class, new Factory<EmiteEventBus>(EmiteEventBus.class) {
	    @Override
	    public EmiteEventBus create() {
		return new DefaultEmiteEventBus();
	    }
	});

	register(Singleton.class, new Factory<XmppConnection>(XmppConnection.class) {
	    @Override
	    public XmppConnection create() {
		return new XmppBoshConnection($(EmiteEventBus.class), $(Services.class));
	    }
	});

	register(Singleton.class, new Factory<Connection>(Connection.class) {
	    @Override
	    public Connection create() {
		return new BoshConnection($(XmppConnection.class));
	    }
	}, new Factory<IMSessionManager>(IMSessionManager.class) {
	    @Override
	    public IMSessionManager create() {
		return new IMSessionManager($(Connection.class));
	    }
	}, new Factory<Session>(Session.class) {
	    @Override
	    public Session create() {
		GWT.log("SESSION CREATED!");
		final SessionImpl session = new SessionImpl($(Connection.class), $(SASLManager.class),
			$(ResourceBindingManager.class), $(IMSessionManager.class));
		return session;
	    }

	    @Override
	    public void onAfterCreated(final Session session) {
		GWT.log("TRIGGER SESSION CREATED");
		$(SessionComponent.class).init();
	    }
	}, new Factory<ResourceBindingManager>(ResourceBindingManager.class) {
	    @Override
	    public ResourceBindingManager create() {
		return new ResourceBindingManager($(EmiteEventBus.class), $(XmppConnection.class));
	    }
	}, new Factory<DecoderRegistry>(DecoderRegistry.class) {
	    @Override
	    public DecoderRegistry create() {
		return new DecoderRegistry();
	    }
	}, new Factory<SASLManager>(SASLManager.class) {
	    @Override
	    public SASLManager create() {
		return new SASLManager($(XmppConnection.class), $(DecoderRegistry.class));
	    }
	});
	register(SessionComponent.class, new Factory<SessionReady>(SessionReady.class) {
	    @Override
	    public SessionReady create() {
		return new SessionReady($(Session.class));
	    }
	});

    }

    public void onModuleLoad() {
	XmppDateTime.useGWT();
	Suco.install(this);
    }

}
