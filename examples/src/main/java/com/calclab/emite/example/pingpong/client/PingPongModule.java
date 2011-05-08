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

package com.calclab.emite.example.pingpong.client;

import com.calclab.emite.browser.client.PageAssist;
import com.calclab.emite.core.client.CoreModule;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.ImModule;
import com.calclab.emite.xep.muc.client.MucModule;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

public class PingPongModule extends AbstractGinModule {

	@Provides
	@Named("other")
	public XmppURI getOtherUri() {
		return XmppURI.uri(PageAssist.getMeta("pingpong.other"));
	}

	@Provides
	@Named("room")
	public XmppURI getRoomUri() {
		return XmppURI.uri(PageAssist.getMeta("pingpong.room"));
	}

	@Override
	protected void configure() {
		install(new CoreModule());
		install(new ImModule());
		install(new MucModule());
		bind(PingPongDisplay.class).to(PingPongWidget.class).in(Singleton.class);
	}
}
