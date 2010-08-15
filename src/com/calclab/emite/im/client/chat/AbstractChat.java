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

import com.calclab.emite.core.client.events.MessageReceivedEvent;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.im.client.chat.events.BeforeReceiveMessageEvent;
import com.calclab.emite.im.client.chat.events.BeforeSendMessageEvent;
import com.calclab.emite.im.client.chat.events.MessageSentEvent;

public abstract class AbstractChat extends ChatBoilerplate {

    public AbstractChat(final XmppSession session, final ChatProperties properties) {
	super(session, properties);

	if (properties.getState() == null) {
	    properties.setState(ChatStates.ready);
	}
	setPreviousChatState(getChatState());

    }

    @Override
    public void close() {

    }

    @Override
    public boolean isReady() {
	return ChatStates.ready.equals(getChatState());
    }

    @Override
    public void open() {

    }

    protected void receive(final Message message) {
	eventBus.fireEvent(new BeforeReceiveMessageEvent(message));
	eventBus.fireEvent(new MessageReceivedEvent(message));
    }

    @Override
    public void send(final Message message) {
	message.setFrom(session.getCurrentUser());
	eventBus.fireEvent(new BeforeSendMessageEvent(message));
	session.send(message);
	eventBus.fireEvent(new MessageSentEvent(message));
    }

}
