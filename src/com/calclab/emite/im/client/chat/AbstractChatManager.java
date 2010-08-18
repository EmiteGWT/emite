package com.calclab.emite.im.client.chat;

import com.calclab.emite.core.client.events.MessageEvent;
import com.calclab.emite.core.client.events.MessageHandler;
import com.calclab.emite.core.client.events.StateChangedEvent;
import com.calclab.emite.core.client.events.StateChangedHandler;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.calclab.emite.core.client.xmpp.session.XmppSession.SessionStates;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;

public abstract class AbstractChatManager extends ChatManagerBoilerplate {
    private XmppURI currentChatUser;

    public AbstractChatManager(final XmppSession session, final ChatSelectionStrategy strategy) {
	super(session, strategy);
	forwardMessagesToChats();
	controlSessionStatus();
    }

    @Override
    public void close(final Chat chat) {
	chat.close();
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
    public Chat openChat(final ChatProperties properties, final boolean createIfNotFound) {
	Chat chat = getChat(properties, false);
	if (chat == null && createIfNotFound) {
	    properties.setInitiatorUri(session.getCurrentUser());
	    chat = addNewChat(properties);
	}
	chat.open();
	fireChatOpened(chat);
	return chat;
    }

    protected void addChat(final Chat chat) {
	chats.add(chat);
    }

    /**
     * This method creates a new chat, add it to the pool and fire the event
     * 
     * @param properties
     */
    protected Chat addNewChat(final ChatProperties properties) {
	final Chat chat = createChat(properties);
	addChat(chat);
	fireChatCreated(chat);
	return chat;
    }

    /**
     * A template method: the subclass must return a new object of class Chat
     * 
     * @param properties
     *            the properties of the chat
     * @return a new chat. must not be null
     */
    protected abstract Chat createChat(ChatProperties properties);

    private void controlSessionStatus() {
	// Control chat state when the user logout and login again
	session.addSessionStateChangedHandler(new StateChangedHandler() {
	    @Override
	    public void onStateChanged(final StateChangedEvent event) {
		if (event.is(SessionStates.loggedIn)) {
		    final XmppURI currentUser = session.getCurrentUser();
		    if (currentChatUser == null) {
			currentChatUser = currentUser;
		    }
		    if (currentUser.equalsNoResource(currentChatUser)) {
			for (Chat chat : chats) {
			    chat.open();
			}
		    }
		} else if (event.is(SessionStates.loggingOut)) {
		    for (Chat chat : chats) {
			chat.close();
		    }
		}
	    }
	}, true);

    }

    private void forwardMessagesToChats() {
	session.addMessageReceivedHandler(new MessageHandler() {
	    @Override
	    public void onMessage(final MessageEvent event) {
		final Message message = event.getMessage();
		final ChatProperties properties = strategy.extractChatProperties(message);
		if (properties != null) {
		    Chat chat = getChat(properties, false);
		    if (chat == null && properties.shouldCreateNewChat()) {
			// we need to create a chat for this incoming message
			properties.setInitiatorUri(properties.getUri());
			chat = addNewChat(properties);
		    }
		    if (chat != null) {
			chat.receive(message);
		    }
		}
	    }
	});
    }

}
