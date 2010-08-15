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

import com.calclab.emite.core.client.events.DefaultEmiteEventBus;
import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.core.client.events.MessageEvent;
import com.calclab.emite.core.client.events.MessageHandler;
import com.calclab.emite.core.client.events.MessageReceivedEvent;
import com.calclab.emite.core.client.events.StateChangedEvent;
import com.calclab.emite.core.client.events.StateChangedHandler;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.calclab.emite.core.client.xmpp.session.XmppSession.SessionState;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.events.BeforeReceiveMessageEvent;
import com.calclab.emite.im.client.chat.events.BeforeSendMessageEvent;
import com.calclab.emite.im.client.chat.events.ChatStateChangedEvent;
import com.calclab.emite.im.client.chat.events.MessageSentEvent;
import com.calclab.suco.client.events.Listener;
import com.google.gwt.event.shared.HandlerRegistration;

public abstract class AbstractChat implements Chat {
    protected final XmppSession session;
    protected final ChatProperties properties;
    private final DefaultEmiteEventBus eventBus;
    private XmppURI currentChatUser;

    public AbstractChat(final XmppSession session, final ChatProperties properties) {
	this.session = session;
	this.properties = properties;
	eventBus = new DefaultEmiteEventBus();
	if (properties.getState() == null) {
	    properties.setState(ChatStates.locked);
	}

	session.addSessionStateChangedHandler(new StateChangedHandler() {
	    @Override
	    public void onStateChanged(final StateChangedEvent event) {
		setStateFromSessionState(session);
	    }
	}, true);
    }

    @Override
    public HandlerRegistration addBeforeReceiveMessageHandler(final MessageHandler handler) {
	return BeforeReceiveMessageEvent.bind(eventBus, handler);
    }

    @Override
    public HandlerRegistration addBeforeSendMessageHandler(final MessageHandler handler) {
	return BeforeSendMessageEvent.bind(eventBus, handler);
    }

    @Override
    public HandlerRegistration addChatStateChangedHandler(final StateChangedHandler handler) {
	return ChatStateChangedEvent.bind(eventBus, handler);
    }

    @Override
    public HandlerRegistration addMessageReceivedHandler(final MessageHandler handler) {
	return MessageReceivedEvent.bind(eventBus, handler);
    }

    @Override
    public HandlerRegistration addMessageSentHandler(final MessageHandler handler) {
	return MessageSentEvent.bind(eventBus, handler);
    }

    @Override
    public EmiteEventBus getChatEventBus() {
	return eventBus;
    }

    @SuppressWarnings("unchecked")
    public <T> T getData(final Class<T> type) {
	return (T) properties.getData(type.toString());
    }

    @Override
    public ChatProperties getProperties() {
	return properties;
    }

    public State getState() {
	final String state = properties.getState();
	try {
	    return State.valueOf(state);
	} catch (final Exception e) {
	    return State.unknown;
	}
    }

    // FIXME: rename to getUri
    public XmppURI getURI() {
	return properties.getUri();
    }

    public boolean isInitiatedByMe() {
	return session.getCurrentUser() != null && session.getCurrentUser().equals(properties.getInitiatorUri());
    }

    public void onBeforeReceive(final Listener<Message> listener) {
	addBeforeReceiveMessageHandler(new MessageHandler() {
	    @Override
	    public void onPacketEvent(final MessageEvent event) {
		listener.onEvent(event.getMessage());
	    }
	});
    }

    public void onBeforeSend(final Listener<Message> listener) {
	addBeforeSendMessageHandler(new MessageHandler() {
	    @Override
	    public void onPacketEvent(final MessageEvent event) {
		listener.onEvent(event.getMessage());
	    }
	});
    }

    public void onMessageReceived(final Listener<Message> listener) {
	addMessageReceivedHandler(new MessageHandler() {
	    @Override
	    public void onPacketEvent(final MessageEvent event) {
		listener.onEvent(event.getMessage());
	    }
	});
    }

    public void onMessageSent(final Listener<Message> listener) {
	addMessageSentHandler(new MessageHandler() {
	    @Override
	    public void onPacketEvent(final MessageEvent event) {
		listener.onEvent(event.getMessage());
	    }
	});
    }

    public void onStateChanged(final Listener<State> listener) {
	addChatStateChangedHandler(new StateChangedHandler() {
	    @Override
	    public void onStateChanged(final StateChangedEvent event) {
		listener.onEvent(getState());
	    }
	});
    }

    public void send(final Message message) {
	message.setFrom(session.getCurrentUser());
	eventBus.fireEvent(new BeforeSendMessageEvent(message));
	session.send(message);
	eventBus.fireEvent(new MessageSentEvent(message));
    }

    @SuppressWarnings("unchecked")
    public <T> T setData(final Class<T> type, final T value) {
	return (T) properties.setData(type.toString(), value);
    }

    private void setStateFromSessionState(final XmppSession session) {
	if (session.isState(SessionState.ready) || session.isState(SessionState.loggedIn)) {
	    final XmppURI currentUser = session.getCurrentUser();
	    if (currentChatUser == null) {
		currentChatUser = currentUser;
	    }
	    setChatState(currentUser.equalsNoResource(currentChatUser) ? ChatStates.ready : ChatStates.locked);
	} else {
	    setChatState(ChatStates.locked);
	}
    }

    protected void fireBeforeReceive(final Message message) {
	eventBus.fireEvent(new BeforeReceiveMessageEvent(message));
    }

    protected String getChatState() {
	return properties.getState();
    }

    protected void receive(final Message message) {
	fireBeforeReceive(message);
	eventBus.fireEvent(new MessageReceivedEvent(message));
    }

    protected void setChatState(final String chatState) {
	if (properties.getState() != chatState) {
	    properties.setState(chatState);
	    session.getEventBus().fireEvent(new ChatStateChangedEvent(chatState));
	}
    }

    @Deprecated
    protected void setState(final State state) {
	setChatState(state.toString());
    }
}
