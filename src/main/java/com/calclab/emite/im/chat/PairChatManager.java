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

package com.calclab.emite.im.chat;

import java.util.Collection;

import javax.annotation.Nullable;

import com.calclab.emite.core.XmppURI;
import com.calclab.emite.im.events.PairChatChangedEvent;
import com.google.web.bindery.event.shared.HandlerRegistration;

public interface PairChatManager {

	/**
	 * Add a handler to track chat changes. The following changes can occur from
	 * a default chat manager: created, opened, closed
	 * 
	 * @param handler
	 */
	HandlerRegistration addPairChatChangedHandler(PairChatChangedEvent.Handler handler);

	/**
	 * The same as getChat, but it fire ChatChanged(opened) event if the chat is
	 * found or created
	 * 
	 * @param uri
	 * @return
	 */
	PairChat openChat(XmppURI uri);

	/**
	 * Same as getChat(new ChatProperties(uri), false);
	 * 
	 * Here for compatibility reasons.
	 * 
	 * @param uri
	 * @return
	 */
	@Nullable
	PairChat getChat(XmppURI uri);
	
	Collection<PairChat> getChats();
}
