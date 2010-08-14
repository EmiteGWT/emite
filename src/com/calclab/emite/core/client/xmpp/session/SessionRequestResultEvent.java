package com.calclab.emite.core.client.xmpp.session;

import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class SessionRequestResultEvent extends GwtEvent<SessionRequestResultHandler> {

    private static final Type<SessionRequestResultHandler> TYPE = new Type<SessionRequestResultHandler>();

    public static HandlerRegistration bind(final EmiteEventBus eventBus, final SessionRequestResultHandler handler) {
	return eventBus.addHandler(TYPE, handler);
    }

    public static Type<SessionRequestResultHandler> getType() {
	return TYPE;
    }

    private final boolean succeed;

    private final XmppURI uri;

    public SessionRequestResultEvent(final XmppURI uri) {
	this(true, uri);
    }

    private SessionRequestResultEvent(final boolean succeed, final XmppURI uri) {
	this.succeed = succeed;
	this.uri = uri;
    }

    @Override
    public Type<SessionRequestResultHandler> getAssociatedType() {
	return TYPE;
    }

    public XmppURI getXmppUri() {
	return uri;
    }

    public boolean isSucceed() {
	return succeed;
    }

    @Override
    protected void dispatch(final SessionRequestResultHandler handler) {
	handler.onSessionRequestResult(this);
    }

}
