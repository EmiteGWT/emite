package com.calclab.emite.core.client.xmpp.resource;

import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class ResourceBindResultEvent extends GwtEvent<ResourceBindResultHandler> {

    private static final Type<ResourceBindResultHandler> TYPE = new Type<ResourceBindResultHandler>();

    /**
     * A helper method to bind handlers of this event to the event bus easily
     * 
     * @param eventBus
     *            the event bus to add the handler to
     * @param handler
     *            the handler to be added
     * @return
     */
    public static HandlerRegistration bind(final EmiteEventBus eventBus, final ResourceBindResultHandler handler) {
	return eventBus.addHandler(TYPE, handler);
    }

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
    public String toDebugString() {
	return super.toDebugString() + xmppUri;
    }

    @Override
    protected void dispatch(final ResourceBindResultHandler handler) {
	handler.onBinded(this);
    }

}
