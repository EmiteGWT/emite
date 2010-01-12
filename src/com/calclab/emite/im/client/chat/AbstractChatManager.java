package com.calclab.emite.im.client.chat;

import java.util.Collection;
import java.util.HashSet;

import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.suco.client.events.Event;
import com.calclab.suco.client.events.Listener;

public abstract class AbstractChatManager implements ChatManager {
    private final HashSet<Chat> chats;
    private final Event<Chat> onChatCreated;
    private final Event<Chat> onChatOpened;
    private final Event<Chat> onChatClosed;
    private final Session session;

    public AbstractChatManager(Session session) {
	this.session = session;
	this.onChatCreated = new Event<Chat>("chatManager.onChatCreated");
	this.onChatClosed = new Event<Chat>("chatManager.onChatClosed");
	this.onChatOpened = new Event<Chat>("chatManager.onChatOpened");
	this.chats = new HashSet<Chat>();
    }

    @Override
    public void close(final Chat chat) {
	getChats().remove(chat);
	fireChatClosed(chat);
    }

    public Collection<? extends Chat> getChats() {
	return chats;
    }

    public void onChatClosed(final Listener<Chat> listener) {
	onChatClosed.add(listener);
    }

    public void onChatCreated(final Listener<Chat> listener) {
	onChatCreated.add(listener);
    }

    public void onChatOpened(Listener<Chat> listener) {
	onChatOpened.add(listener);
    }

    public Chat open(final XmppURI uri) {
	Chat chat = findChat(uri);
	if (chat == null) {
	    chat = createChat(uri, getSession().getCurrentUser());
	    addChat(chat);
	    fireChatCreated(chat);
	}
	fireChatOpened(chat);
	return chat;
    }

    protected void addChat(Chat chat) {
	chats.add(chat);
    }

    protected abstract Chat createChat(XmppURI uri, XmppURI currentUser);

    protected abstract Chat findChat(XmppURI uri);

    protected void fireChatClosed(Chat chat) {
	onChatClosed.fire(chat);
    }

    protected void fireChatCreated(Chat chat) {
	onChatCreated.fire(chat);
    }

    protected void fireChatOpened(Chat chat) {
	onChatOpened.fire(chat);
    }

    protected Session getSession() {
	return session;
    }
}
