package com.calclab.emite.im.client.roster;

import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.calclab.emite.im.client.roster.events.RosterGroupChangedEvent;
import com.calclab.emite.im.client.roster.events.RosterGroupChangedHandler;
import com.calclab.emite.im.client.roster.events.RosterItemChangedEvent;
import com.calclab.emite.im.client.roster.events.RosterItemChangedHandler;
import com.calclab.emite.im.client.roster.events.RosterRetrievedEvent;
import com.calclab.emite.im.client.roster.events.RosterRetrievedHandler;
import com.google.gwt.event.shared.HandlerRegistration;

public abstract class XmppRosterBoilerplate implements XmppRoster {

    protected final XmppSession session;
    protected EmiteEventBus eventBus;
    protected boolean rosterReady;

    public XmppRosterBoilerplate(XmppSession session) {
	this.session = session;
	this.eventBus = session.getEventBus();
	rosterReady = false;
    }

    @Override
    public HandlerRegistration addRosterGroupChangedHandler(RosterGroupChangedHandler handler) {
	return RosterGroupChangedEvent.bind(eventBus, handler);
    }

    @Override
    public HandlerRegistration addRosterItemChangedHandler(RosterItemChangedHandler handler) {
	return RosterItemChangedEvent.bind(eventBus, handler);
    }

    @Override
    public HandlerRegistration addRosterRetrievedHandler(RosterRetrievedHandler handler) {
	return RosterRetrievedEvent.bind(eventBus, handler);
    }

    @Override
    public boolean isRosterReady() {
	return rosterReady;
    }

}
