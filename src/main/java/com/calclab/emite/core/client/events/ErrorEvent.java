package com.calclab.emite.core.client.events;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.MatcherFactory;
import com.calclab.emite.core.client.packet.NoPacket;
import com.calclab.emite.core.client.packet.PacketMatcher;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * An error has occurred. If is an error from an incoming stanza, the getStanza
 * and getErrorStanza methods returns something different from NoPacket.INSTANCE
 */
public class ErrorEvent extends GwtEvent<ErrorHandler> {

    private static final PacketMatcher ERROR_STANZA_MATCHER = MatcherFactory.byName("error");
    private static final Type<ErrorHandler> TYPE = new Type<ErrorHandler>();

    public static HandlerRegistration bind(EmiteEventBus eventBus, ErrorHandler handler) {
	return eventBus.addHandler(TYPE, handler);
    }
    private final String errorType;
    private final String description;
    private final IPacket stanza;
    private final IPacket errorStanza;

    public ErrorEvent(String errorType, String description, IPacket stanza) {
	this.errorType = errorType;
	this.description = description;
	this.stanza = stanza != null ? stanza : NoPacket.INSTANCE;
	this.errorStanza = this.stanza.getFirstChild(ERROR_STANZA_MATCHER);
    }

    @Override
    public Type<ErrorHandler> getAssociatedType() {
	return TYPE;
    }

    public String getDescription() {
	return description;
    }

    /**
     * The associated error stanza if any
     * 
     * @return the error stanza of NoPacket.INSTANCE if none
     * @see http://xmpp.org/rfcs/rfc3920.html#rfc.section.4.7
     */
    public IPacket getErrorStanza() {
	return errorStanza;
    }

    public String getErrorType() {
	return errorType;
    }

    /**
     * The server side stanza that fired the error event
     * 
     * @return never null: NoPacket.INSTANCE if the is a client side error
     */
    public IPacket getStanza() {
	return stanza;
    }

    @Override
    protected void dispatch(ErrorHandler handler) {
	handler.onError(this);
    }

}
