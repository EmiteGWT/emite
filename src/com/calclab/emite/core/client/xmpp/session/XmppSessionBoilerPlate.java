package com.calclab.emite.core.client.xmpp.session;

import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.core.client.events.IQEvent;
import com.calclab.emite.core.client.events.IQHandler;
import com.calclab.emite.core.client.events.MessageHandler;
import com.calclab.emite.core.client.events.MessageReceivedEvent;
import com.calclab.emite.core.client.events.PresenceHandler;
import com.calclab.emite.core.client.events.PresenceReceivedEvent;
import com.calclab.emite.core.client.events.StateChangedHandler;
import com.calclab.emite.core.client.xmpp.stanzas.IQ;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.Presence;
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
    public HandlerRegistration addIQReceivedHandler(final IQHandler handler) {
	return eventBus.addHandler(IQEvent.getType(), handler);
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

    public void setSessionState(final String state) {
	this.state = state;
	eventBus.fireEvent(new SessionStateChangedEvent(state));
    }

    protected void fireIQ(final IQ iq) {
	eventBus.fireEvent(new IQEvent(iq));
    }

    protected void fireMessage(final Message message) {
	eventBus.fireEvent(new MessageReceivedEvent(message));
    }

    protected void firePresence(final Presence presence) {
	eventBus.fireEvent(new PresenceReceivedEvent(presence));
    }
}
