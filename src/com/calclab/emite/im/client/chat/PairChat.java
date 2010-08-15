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
package com.calclab.emite.im.client.chat;

import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.Message.Type;

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
public class PairChat extends AbstractChat {
    private static final String PAIRCHAT_THREAD_PROP = "pairchat.thred";
    private final String id;

    public PairChat(final XmppSession session, final ChatProperties properties) {
	this(session, properties, new PairChatSelectionStrategy());
    }

    public PairChat(final XmppSession session, final ChatProperties properties, ChatSelectionStrategy strategy) {
	super(session, properties, strategy);
	id = generateChatID();
    }

    @Override
    public boolean equals(final Object obj) {
	if (obj == null) {
	    return false;
	}
	if (this == obj) {
	    return true;
	}
	final PairChat other = (PairChat) obj;
	return id.equals(other.id);
    }

    private String generateChatID() {
	return "chat: " + getURI().toString() + "-" + getThread();
    }

    @Override
    public String getID() {
	return id;
    }

    public String getThread() {
	return (String) properties.getData(PAIRCHAT_THREAD_PROP);
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + (getURI() == null ? 0 : getURI().hashCode());
	final String thread = getThread();
	result = prime * result + (thread == null ? 0 : thread.hashCode());
	return result;
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

}
