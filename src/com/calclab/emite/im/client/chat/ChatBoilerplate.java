package com.calclab.emite.im.client.chat;

import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.core.client.events.ErrorEvent;
import com.calclab.emite.core.client.events.ErrorHandler;
import com.calclab.emite.core.client.events.EventBusFactory;
import com.calclab.emite.core.client.events.MessageEvent;
import com.calclab.emite.core.client.events.MessageHandler;
import com.calclab.emite.core.client.events.MessageReceivedEvent;
import com.calclab.emite.core.client.events.StateChangedEvent;
import com.calclab.emite.core.client.events.StateChangedHandler;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.events.BeforeReceiveMessageEvent;
import com.calclab.emite.im.client.chat.events.BeforeSendMessageEvent;
import com.calclab.emite.im.client.chat.events.ChatStateChangedEvent;
import com.calclab.emite.im.client.chat.events.MessageSentEvent;
import com.calclab.suco.client.events.Listener;
import com.google.gwt.event.shared.HandlerRegistration;

public abstract class ChatBoilerplate implements Chat {
    protected final XmppSession session;
    protected final ChatProperties properties;
    protected final EmiteEventBus chatEventBus;

    private static final String PREVIOUS_CHAT_STATE = "chatstate.previous";

    public ChatBoilerplate(XmppSession session, ChatProperties properties) {
	this.session = session;
	this.properties = properties;
	chatEventBus = EventBusFactory.create(properties.getUri().toString());
    }

    @Override
    public HandlerRegistration addBeforeReceiveMessageHandler(final MessageHandler handler) {
	return BeforeReceiveMessageEvent.bind(chatEventBus, handler);
    }

    @Override
    public HandlerRegistration addBeforeSendMessageHandler(final MessageHandler handler) {
	return BeforeSendMessageEvent.bind(chatEventBus, handler);
    }

    @Override
    public HandlerRegistration addChatStateChangedHandler(boolean sendCurrent, final StateChangedHandler handler) {
	if (sendCurrent) {
	    handler.onStateChanged(new ChatStateChangedEvent(getChatState()));
	}
	return ChatStateChangedEvent.bind(chatEventBus, handler);
    }

    @Override
    public HandlerRegistration addErrorHandler(ErrorHandler handler) {
	return ErrorEvent.bind(chatEventBus, handler);
    }

    @Override
    public HandlerRegistration addMessageReceivedHandler(final MessageHandler handler) {
	return MessageReceivedEvent.bind(chatEventBus, handler);
    }

    @Override
    public HandlerRegistration addMessageSentHandler(final MessageHandler handler) {
	return MessageSentEvent.bind(chatEventBus, handler);
    }

    @Override
    public EmiteEventBus getChatEventBus() {
	return chatEventBus;
    }

    @Override
    public String getChatState() {
	return properties.getState();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getData(final Class<T> type) {
	return (T) properties.getData(type.toString());
    }

    @Override
    public ChatProperties getProperties() {
	return properties;
    }

    @Override
    public XmppSession getSession() {
	return session;
    }

    /**
     * Use getChatState
     */
    @Override
    @Deprecated
    public State getState() {
	final String state = properties.getState();
	try {
	    return State.valueOf(state);
	} catch (final Exception e) {
	    return State.unknown;
	}
    }

    // FIXME: rename to getUri
    @Override
    public XmppURI getURI() {
	return properties.getUri();
    }

    @Override
    public boolean isInitiatedByMe() {
	return session.getCurrentUserURI() != null && session.getCurrentUserURI().equals(properties.getInitiatorUri());
    }

    @Override
    @Deprecated
    public void onBeforeReceive(final Listener<Message> listener) {
	addBeforeReceiveMessageHandler(new MessageHandler() {
	    @Override
	    public void onMessage(final MessageEvent event) {
		listener.onEvent(event.getMessage());
	    }
	});
    }

    @Override
    @Deprecated
    public void onBeforeSend(final Listener<Message> listener) {
	addBeforeSendMessageHandler(new MessageHandler() {
	    @Override
	    public void onMessage(final MessageEvent event) {
		listener.onEvent(event.getMessage());
	    }
	});
    }

    @Override
    @Deprecated
    public void onMessageReceived(final Listener<Message> listener) {
	addMessageReceivedHandler(new MessageHandler() {
	    @Override
	    public void onMessage(final MessageEvent event) {
		listener.onEvent(event.getMessage());
	    }
	});
    }

    @Override
    @Deprecated
    public void onMessageSent(final Listener<Message> listener) {
	addMessageSentHandler(new MessageHandler() {
	    @Override
	    public void onMessage(final MessageEvent event) {
		listener.onEvent(event.getMessage());
	    }
	});
    }

    @Override
    @Deprecated
    public void onStateChanged(final Listener<State> listener) {
	addChatStateChangedHandler(false, new StateChangedHandler() {
	    @Override
	    public void onStateChanged(final StateChangedEvent event) {
		listener.onEvent(getState());
	    }
	});
    }

    @Override
    @Deprecated
    @SuppressWarnings("unchecked")
    public <T> T setData(final Class<T> type, final T value) {
	String key = type != null ? type.toString() : null;
	return (T) properties.setData(key, value);
    }

    protected String getPreviousChatState() {
	return (String) properties.getData(PREVIOUS_CHAT_STATE);
    }

    protected void setPreviousChatState(String chatState) {
	properties.setData(PREVIOUS_CHAT_STATE, chatState);
    }

}
