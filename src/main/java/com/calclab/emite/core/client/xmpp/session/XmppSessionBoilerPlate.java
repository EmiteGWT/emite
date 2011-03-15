package com.calclab.emite.core.client.xmpp.session;

import com.calclab.emite.core.client.events.BeforeStanzaSendEvent;
import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.core.client.events.IQHandler;
import com.calclab.emite.core.client.events.IQReceivedEvent;
import com.calclab.emite.core.client.events.MessageHandler;
import com.calclab.emite.core.client.events.MessageReceivedEvent;
import com.calclab.emite.core.client.events.PacketHandler;
import com.calclab.emite.core.client.events.PresenceHandler;
import com.calclab.emite.core.client.events.PresenceReceivedEvent;
import com.calclab.emite.core.client.events.StateChangedHandler;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.google.gwt.event.shared.HandlerRegistration;

public abstract class XmppSessionBoilerPlate implements XmppSession {
    protected final EmiteEventBus eventBus;
    private String state;

    public XmppSessionBoilerPlate(final EmiteEventBus eventBus) {
	this.eventBus = eventBus;
	state = SessionStates.disconnected;
    }

    @Override
    public HandlerRegistration addBeforeSendStanzaHandler(PacketHandler handler) {
	return BeforeStanzaSendEvent.bind(eventBus, handler);
    }

    @Override
    public HandlerRegistration addIQReceivedHandler(final IQHandler handler) {
	return IQReceivedEvent.bind(eventBus, handler);
    }

    @Override
    public HandlerRegistration addMessageReceivedHandler(final MessageHandler handler) {
	return MessageReceivedEvent.bind(eventBus, handler);
    }

    @Override
    public HandlerRegistration addPresenceReceivedHandler(final PresenceHandler handler) {
	return PresenceReceivedEvent.bind(eventBus, handler);
    }

    @Override
    public HandlerRegistration addSessionStateChangedHandler(final boolean sendCurrent,
	    final StateChangedHandler handler) {
	if (sendCurrent) {
	    handler.onStateChanged(new SessionStateChangedEvent(getSessionState()));
	}
	return SessionStateChangedEvent.bind(eventBus, handler);
    }

    @Override
    public EmiteEventBus getEventBus() {
	return eventBus;
    }

    @Override
    public String getSessionState() {
	return state;
    }

    @Override
    public boolean isState(final String expectedState) {
	return state.equals(expectedState);
    }

    @Override
    public void login(final XmppURI uri, final String password) {
	login(new Credentials(uri, password, Credentials.ENCODING_NONE));
    }

    @Override
    public void setSessionState(final String newState) {
	if (!newState.equals(state)) {
	    this.state = newState;
	    eventBus.fireEvent(new SessionStateChangedEvent(newState));
	}
    }
}
