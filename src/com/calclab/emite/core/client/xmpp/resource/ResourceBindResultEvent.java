package com.calclab.emite.core.client.xmpp.resource;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.google.gwt.event.shared.GwtEvent;

public class ResourceBindResultEvent extends GwtEvent<ResourceBindResultHandler> {

    private static final Type<ResourceBindResultHandler> TYPE = new Type<ResourceBindResultHandler>();

    public static Type<ResourceBindResultHandler> getType() {
	return TYPE;
    }

    private final XmppURI xmppUri;

    public ResourceBindResultEvent(final XmppURI xmppUri) {
	this.xmppUri = xmppUri;
    }

    @Override
    public Type<ResourceBindResultHandler> getAssociatedType() {
	return TYPE;
    }

    public XmppURI getXmppUri() {
	return xmppUri;
    }

    @Override
    protected void dispatch(final ResourceBindResultHandler handler) {
	handler.onBinded(this);
    }

}
