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

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.NoPacket;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.core.client.xmpp.stanzas.Message.Type;
import com.calclab.emite.im.client.chat.Chat.State;
import com.calclab.suco.client.events.Listener;

/**
 * Default ChatManager implementation. Use ChatManager interface instead
 * 
 * @see ChatManager
 */
public class PairChatManager extends AbstractChatManager implements ChatManager {
    public PairChatManager(final Session session) {
	super(session);
	session.onMessage(new Listener<Message>() {
	    public void onEvent(final Message message) {
		eventMessage(message);
	    }
	});

    }

    @Override
    public void close(final Chat chat) {
	((AbstractChat) chat).setState(State.locked);
	super.close(chat);
    }

    /**
     * Create a new chat
     * 
     * @param toURI
     * @param starterURI
     * @return
     */
    @Override
    protected Chat createChat(final XmppURI toURI, final XmppURI starterURI) {
	final PairChat pairChat = new PairChat(session, toURI, starterURI, null);
	pairChat.setState(Chat.State.ready);
	return pairChat;
    }

    /**
     * Template method. Should be protected to be overriden by Room Manager
     * Currently it filters all the Messages without body. (see issue #114)
     * 
     * @param message
     */
    protected void eventMessage(final Message message) {
	final Type type = message.getType();
	switch (type) {
	case chat:
	case normal:
	    final IPacket body = message.getFirstChild("body");
	    if (body != NoPacket.INSTANCE) {
		final XmppURI from = message.getFrom();

		Chat chat = findChat(from);
		if (chat == null) {
		    chat = createChat(from, from);
		    addChat(chat);
		    fireChatCreated(chat);
		}
	    }
	    break;
	}
    }

    /**
     * Find a chat using the given uri.
     */
    @Override
    protected Chat findChat(final XmppURI uri) {
	for (final Chat chat : getChats()) {
	    final XmppURI chatTargetURI = chat.getURI();
	    if (uri.equalsNoResource(chatTargetURI)) {
		return chat;
	    }
	}
	return null;
    }

}
