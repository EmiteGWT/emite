package com.calclab.emite.core.client.xmpp.sasl;

import com.calclab.emite.core.client.xmpp.session.Credentials;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.google.gwt.event.shared.GwtEvent;

public class AuthorizationResultEvent extends GwtEvent<AuthorizationResultHandler> {
    private static final Type<AuthorizationResultHandler> TYPE = new Type<AuthorizationResultHandler>();

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
    protected void dispatch(final AuthorizationResultHandler handler) {
	handler.onAuthorization(this);
    }

}
