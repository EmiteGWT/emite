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

package com.calclab.emite.example.echo.client;

import com.calclab.emite.browser.client.BrowserModule;
import com.calclab.emite.core.client.CoreModule;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.calclab.emite.im.client.ImModule;
import com.calclab.emite.im.client.chat.ChatManager;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules({ CoreModule.class, ImModule.class, BrowserModule.class })
interface EchoGinjector extends Ginjector {
	XmppSession getXmppSession();

	ChatManager getChatManager();
}