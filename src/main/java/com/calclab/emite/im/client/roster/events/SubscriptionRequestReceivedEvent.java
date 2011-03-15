package com.calclab.emite.im.client.roster.events;

import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class SubscriptionRequestReceivedEvent extends GwtEvent<SubscriptionRequestReceivedHandler> {

    private static final Type<SubscriptionRequestReceivedHandler> TYPE = new Type<SubscriptionRequestReceivedHandler>();

    public static HandlerRegistration bind(EmiteEventBus eventBus, SubscriptionRequestReceivedHandler handler) {
	return eventBus.addHandler(TYPE, handler);
    }
    private final String nick;

    private final XmppURI from;

    public SubscriptionRequestReceivedEvent(XmppURI from, String nick) {
	this.from = from;
	this.nick = nick;
    }

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<SubscriptionRequestReceivedHandler> getAssociatedType() {
	return TYPE;
    }

    public XmppURI getFrom() {
	return from;
    }

    public String getNick() {
	return nick;
    }

    @Override
    protected void dispatch(SubscriptionRequestReceivedHandler handler) {
	handler.onSubscriptionRequestReceived(this);
    }

}
