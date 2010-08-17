package com.calclab.emite.im.client.chat;

import java.util.Collection;
import java.util.HashSet;

import com.calclab.emite.core.client.events.GwtEmiteEventBus;
import com.calclab.emite.core.client.events.ChangedEvent.ChangeEventTypes;
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
    protected final GwtEmiteEventBus managerEventBus;

    public ChatManagerBoilerplate(XmppSession session, ChatSelectionStrategy strategy) {
	this.session = session;
	this.strategy = strategy;
	this.managerEventBus = new GwtEmiteEventBus();
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

    @Override
    // TODO: deprecate
    public void onChatClosed(final Listener<Chat> listener) {
	ChatChangedEvent.bind(session.getEventBus(), new ChatChangedHandler() {
	    @Override
	    public void onChatChanged(final ChatChangedEvent event) {
		if (event.isClosed()) {
		    listener.onEvent(event.getChat());
		}
	    }
	});
    }

    @Override
    // TODO: deprecate
    public void onChatCreated(final Listener<Chat> listener) {
	ChatChangedEvent.bind(session.getEventBus(), new ChatChangedHandler() {
	    @Override
	    public void onChatChanged(final ChatChangedEvent event) {
		if (event.isCreated()) {
		    listener.onEvent(event.getChat());
		}
	    }
	});
    }

    @Override
    // TODO: deprecate
    public void onChatOpened(final Listener<Chat> listener) {
	ChatChangedEvent.bind(session.getEventBus(), new ChatChangedHandler() {
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

    protected void fireChatClosed(final Chat chat) {
	managerEventBus.fireEvent(new ChatChangedEvent(ChangeEventTypes.closed, chat));
    }

    protected void fireChatCreated(final Chat chat) {
	managerEventBus.fireEvent(new ChatChangedEvent(ChangeEventTypes.created, chat));
    }

    protected void fireChatOpened(final Chat chat) {
	managerEventBus.fireEvent(new ChatChangedEvent(ChangeEventTypes.opened, chat));
    }

}
