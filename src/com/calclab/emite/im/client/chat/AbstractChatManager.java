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
    protected final Session session;

    public AbstractChatManager(final Session session) {
	this.session = session;
	onChatCreated = new Event<Chat>("chatManager.onChatCreated");
	onChatClosed = new Event<Chat>("chatManager.onChatClosed");
	onChatOpened = new Event<Chat>("chatManager.onChatOpened");
	chats = new HashSet<Chat>();
    }

    @Override
    public void close(final Chat chat) {
	getChats().remove(chat);
	fireChatClosed(chat);
    }

    public abstract Chat getChat(XmppURI uri);

    public Collection<? extends Chat> getChats() {
	return chats;
    }

    public void onChatClosed(final Listener<Chat> listener) {
	onChatClosed.add(listener);
    }

    public void onChatCreated(final Listener<Chat> listener) {
	onChatCreated.add(listener);
    }

    public void onChatOpened(final Listener<Chat> listener) {
	onChatOpened.add(listener);
    }

    public Chat open(final XmppURI uri) {
	Chat chat = getChat(uri);
	if (chat == null) {
	    chat = createChat(uri, session.getCurrentUser());
	    addChat(chat);
	    fireChatCreated(chat);
	}
	fireChatOpened(chat);
	return chat;
    }

    protected void addChat(final Chat chat) {
	chats.add(chat);
    }

    protected abstract Chat createChat(XmppURI uri, XmppURI currentUser);

    protected void fireChatClosed(final Chat chat) {
	onChatClosed.fire(chat);
    }

    protected void fireChatCreated(final Chat chat) {
	onChatCreated.fire(chat);
    }

    protected void fireChatOpened(final Chat chat) {
	onChatOpened.fire(chat);
    }
}
