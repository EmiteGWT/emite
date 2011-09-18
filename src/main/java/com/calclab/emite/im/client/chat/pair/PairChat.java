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

package com.calclab.emite.im.client.chat.pair;

import com.calclab.emite.core.client.session.XmppSession;
import com.calclab.emite.core.client.stanzas.Message;
import com.calclab.emite.core.client.stanzas.Message.Type;
import com.calclab.emite.im.client.chat.ChatBoilerplate;
import com.calclab.emite.im.client.chat.Chat;
import com.calclab.emite.im.client.chat.ChatProperties;
import com.calclab.emite.im.client.chat.ChatStatus;
import com.google.web.bindery.event.shared.EventBus;

/**
 * Default Chat implementation. Use Chat interface instead. Created by a chat
 * manager
 * 
 * About Chat ids: Other sender Uri plus thread identifies a chat (associated
 * with a chat panel in the UI). If no thread is specified, we join all messages
 * in one chat.
 * 
 * @see Chat
 */
public class PairChat extends ChatBoilerplate {
	
	private static final String PAIRCHAT_THREAD_PROP = "pairchat.thread";
	
	private final String id;

	/**
	 * Create a new pair chat. Pair chats are created by PairChatManager
	 * 
	 * @param session
	 * @param properties
	 */
	protected PairChat(final EventBus eventBus, final XmppSession session, final ChatProperties properties) {
		super(eventBus, session, properties);
		id = generateChatID();
	}

	@Override
	public String getID() {
		return id;
	}

	public String getThread() {
		return (String) properties.getData(PAIRCHAT_THREAD_PROP);
	}

	@Override
	public void open() {
		if (session.isReady()) {
			setStatus(ChatStatus.ready);
		}
	}

	@Override
	public void send(final Message message) {
		message.setThread(getThread());
		message.setType(Type.chat);
		message.setTo(getURI());
		super.send(message);
	}

	public void setThread(final String thread) {
		properties.setData(PAIRCHAT_THREAD_PROP, thread);
	}

	@Override
	public String toString() {
		return id;
	}

	private String generateChatID() {
		return "chat: " + getURI().toString() + "-" + getThread();
	}
}
