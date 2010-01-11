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

import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.core.client.xmpp.stanzas.Message.Type;
import com.calclab.suco.client.events.Listener;

/**
 * <p>
 * Default Chat implementation. Use Chat interface instead
 * </p>
 * 
 * <p>
 * About Chat ids: Other sender Uri plus thread identifies a chat (associated
 * with a chat panel in the UI). If no thread is specified, we join all messages
 * in one chat.
 * </p>
 * 
 * @see Chat
 */
public class PairChat extends AbstractChat {
    protected final String thread;
    private final String id;
    private XmppURI user;

    PairChat(final Session session, final XmppURI other, final XmppURI starter, final String thread) {
	super(session, other, starter);
	this.thread = thread;
	this.id = generateChatID();

	setStateFromSessionState(session);

	session.onStateChanged(new Listener<Session>() {
	    @Override
	    public void onEvent(Session session) {
		setStateFromSessionState(session);
	    }
	});

	session.onMessage(new Listener<Message>() {
	    public void onEvent(final Message message) {
		final XmppURI from = message.getFrom();
		if (from.equalsNoResource(uri)) {
		    receive(message);
		}
	    }
	});
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

    public String getID() {
	return id;
    }

    public String getThread() {
	return thread;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + (uri == null ? 0 : uri.hashCode());
	result = prime * result + (thread == null ? 0 : thread.hashCode());
	return result;
    }

    @Override
    public void send(final Message message) {
	message.setThread(thread);
	message.setType(Type.chat);
	message.setTo(uri);
	super.send(message);
    }

    @Override
    public String toString() {
	return id;
    }

    private String generateChatID() {
	return "chat: " + uri.toString() + "-" + thread;
    }

    private void setStateFromSessionState(final Session session) {
	switch (session.getState()) {
	case loggedIn:
	case ready:
	    final XmppURI currentUser = session.getCurrentUser();
	    if (this.user == null) {
		this.user = currentUser;
	    }
	    setState(currentUser.equalsNoResource(user) ? State.ready : State.locked);
	    break;
	default:
	    setState(State.locked);
	    break;
	}
    }

}
