package com.calclab.emite.core.client.xmpp.sasl;

import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.core.client.xmpp.session.Credentials;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class AuthorizationResultEvent extends GwtEvent<AuthorizationResultHandler> {
    private static final Type<AuthorizationResultHandler> TYPE = new Type<AuthorizationResultHandler>();

    public static HandlerRegistration bind(final EmiteEventBus eventBus, final AuthorizationResultHandler handler) {
	return eventBus.addHandler(TYPE, handler);
    }

    public static Type<AuthorizationResultHandler> getType() {
	return TYPE;
    }
    private final boolean succeed;

    private final Credentials credentials;

    /**
     * Build a failed authorization event
     */
    public AuthorizationResultEvent() {
	this(false, null);
    }

    /**
     * Build a succeeded authorization event with the current credentials
     * 
     * @param uri
     *            the uri of the authorized user
     */
    public AuthorizationResultEvent(final Credentials credentials) {
	this(true, credentials);
    }

    private AuthorizationResultEvent(final boolean succeed, final Credentials credentials) {
	this.succeed = succeed;
	this.credentials = credentials;
    }

    @Override
    public Type<AuthorizationResultHandler> getAssociatedType() {
	return TYPE;
    }

    public Credentials getCredentials() {
	return credentials;
    }

    public XmppURI getXmppUri() {
	return credentials.getXmppUri();
    }

    public boolean isSucceed() {
	return succeed;
    }

    @Override
    public String toDebugString() {
	final String value = succeed ? " Succeed - " + credentials.getXmppUri() : " Failed!";
	return super.toDebugString() + value;
    }

    @Override
    protected void dispatch(final AuthorizationResultHandler handler) {
	handler.onAuthorization(this);
    }

}
