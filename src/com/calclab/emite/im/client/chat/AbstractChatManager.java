package com.calclab.emite.im.client.chat;

import java.util.Collection;
import java.util.HashSet;

import com.calclab.emite.core.client.events.MessageEvent;
import com.calclab.emite.core.client.events.MessageHandler;
import com.calclab.emite.core.client.events.MessageReceivedEvent;
import com.calclab.emite.core.client.events.ChangedEvent.ChangeAction;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.events.ChatChangedEvent;
import com.calclab.emite.im.client.chat.events.ChatChangedHandler;
import com.calclab.suco.client.events.Listener;
import com.google.gwt.event.shared.HandlerRegistration;

public abstract class AbstractChatManager implements ChatManager {
    private final HashSet<Chat> chats;
    protected final XmppSession session;
    protected ChatSelectionStrategy strategy;

    public AbstractChatManager(final XmppSession session, final ChatSelectionStrategy strategy) {
	this.session = session;
	this.strategy = strategy;
	chats = new HashSet<Chat>();

	session.addMessageReceivedHandler(new MessageHandler() {
	    @Override
	    public void onPacketEvent(final MessageEvent event) {
		final Message message = event.getMessage();
		final ChatProperties properties = strategy.extractChatProperties(message);
		Chat chat = getChat(properties, false);
		if (chat == null) {
		    // we need to create a chat for this incoming message
		    properties.setInitiatorUri(properties.getUri());
		    chat = addNewChat(properties);
		}
		chat.getChatEventBus().fireEvent(new MessageReceivedEvent(message));
	    }
	});
    }

    @Override
    public HandlerRegistration addChatChangedHandler(final ChatChangedHandler handler) {
	return ChatChangedEvent.bind(session.getEventBus(), handler);
    }

    @Override
    public void close(final Chat chat) {
	getChats().remove(chat);
	fireChatClosed(chat);
    }

    @Override
    public Chat getChat(final ChatProperties properties, final boolean createIfNotFound) {
	for (final Chat chat : chats) {
	    if (strategy.isAssignable(chat, properties)) {
		return chat;
	    }
	}
	if (createIfNotFound) {
	}
	return null;
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
    public Chat openChat(final ChatProperties properties, final boolean createIfNotFound) {
	Chat chat = getChat(properties, false);
	if (chat == null && createIfNotFound) {
	    properties.setInitiatorUri(session.getCurrentUser());
	    chat = addNewChat(properties);
	}
	fireChatOpened(chat);
	return chat;
    }

    @Override
    public void setChatSelectionStrategy(final ChatSelectionStrategy strategy) {
	assert strategy != null : "The ChatSelectionStrategy can't be null!";
	this.strategy = strategy;
    }

    /**
     * This method creates a new chat, add it to the pool and fire the event
     * 
     * @param properties
     */
    private Chat addNewChat(final ChatProperties properties) {
	final Chat chat = createChat(properties);
	addChat(chat);
	fireChatCreated(chat);
	return chat;
    }

    protected void addChat(final Chat chat) {
	chats.add(chat);
    }

    /**
     * A template method: the subclass must return a new object of class Chat
     * 
     * @param properties
     *            the properties of the chat
     * @return a new chat. must not be null
     */
    protected abstract Chat createChat(ChatProperties properties);

    protected void fireChatClosed(final Chat chat) {
	session.getEventBus().fireEvent(new ChatChangedEvent(ChangeAction.closed, chat));
    }

    protected void fireChatCreated(final Chat chat) {
	session.getEventBus().fireEvent(new ChatChangedEvent(ChangeAction.created, chat));
    }

    protected void fireChatOpened(final Chat chat) {
	session.getEventBus().fireEvent(new ChatChangedEvent(ChangeAction.opened, chat));
    }
}
