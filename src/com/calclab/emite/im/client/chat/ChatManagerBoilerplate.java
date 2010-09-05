package com.calclab.emite.im.client.chat;

import java.util.Collection;
import java.util.HashSet;

import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.core.client.events.EventBusFactory;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.events.ChatChangedEvent;
import com.calclab.emite.im.client.chat.events.ChatChangedHandler;
import com.calclab.suco.client.events.Listener;
import com.google.gwt.event.shared.HandlerRegistration;

public abstract class ChatManagerBoilerplate implements ChatManager {

    protected final XmppSession session;
    protected ChatSelectionStrategy strategy;
    protected final HashSet<Chat> chats;
    protected final EmiteEventBus managerEventBus;

    public ChatManagerBoilerplate(XmppSession session, ChatSelectionStrategy strategy) {
	this.session = session;
	this.strategy = strategy;
	this.managerEventBus = EventBusFactory.create("chatManager");
	chats = new HashSet<Chat>();
    }

    @Override
    public HandlerRegistration addChatChangedHandler(final ChatChangedHandler handler) {
	return ChatChangedEvent.bind(managerEventBus, handler);
    }

    @Override
    public Chat getChat(final XmppURI uri) {
	return getChat(new ChatProperties(uri), false);
    }

    @Override
    public Collection<? extends Chat> getChats() {
	return chats;
    }

    /**
     * Use addChatChangedHandler
     */
    @Override
    @Deprecated
    public void onChatClosed(final Listener<Chat> listener) {
	ChatChangedEvent.bind(managerEventBus, new ChatChangedHandler() {
	    @Override
	    public void onChatChanged(final ChatChangedEvent event) {
		if (event.isClosed()) {
		    listener.onEvent(event.getChat());
		}
	    }
	});
    }

    @Override
    @Deprecated
    public void onChatCreated(final Listener<Chat> listener) {
	ChatChangedEvent.bind(managerEventBus, new ChatChangedHandler() {
	    @Override
	    public void onChatChanged(final ChatChangedEvent event) {
		if (event.isCreated()) {
		    listener.onEvent(event.getChat());
		}
	    }
	});
    }

    @Override
    @Deprecated
    public void onChatOpened(final Listener<Chat> listener) {
	ChatChangedEvent.bind(managerEventBus, new ChatChangedHandler() {
	    @Override
	    public void onChatChanged(final ChatChangedEvent event) {
		if (event.isOpened()) {
		    listener.onEvent(event.getChat());
		}
	    }
	});
    }

    @Override
    public Chat open(final XmppURI uri) {
	return openChat(new ChatProperties(uri), true);
    }

    @Override
    public void setChatSelectionStrategy(final ChatSelectionStrategy strategy) {
	assert strategy != null : "The ChatSelectionStrategy can't be null!";
	this.strategy = strategy;
    }

}
