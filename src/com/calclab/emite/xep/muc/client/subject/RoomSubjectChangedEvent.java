package com.calclab.emite.xep.muc.client.subject;

import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * An event to inform about room subject changes
 * 
 * @author dani
 * 
 */
public class RoomSubjectChangedEvent extends GwtEvent<RoomSubjectChangedHandler> {

    private static final Type<RoomSubjectChangedHandler> TYPE = new Type<RoomSubjectChangedHandler>();

    public static HandlerRegistration bind(EmiteEventBus eventBus, RoomSubjectChangedHandler handler) {
	return eventBus.addHandler(TYPE, handler);
    }

    private final String subject;
    private final XmppURI occupantUri;

    public RoomSubjectChangedEvent(XmppURI occupantUri, String subject) {
	this.occupantUri = occupantUri;
	this.subject = subject;
    }

    @Override
    public Type<RoomSubjectChangedHandler> getAssociatedType() {
	return TYPE;
    }

    /**
     * Get modificator's occupant (room and nick) uri
     * 
     * @return
     */
    public XmppURI getOccupantUri() {
	return occupantUri;
    }

    /**
     * The new subject
     * 
     * @return
     */
    public String getSubject() {
	return subject;
    }

    @Override
    protected void dispatch(RoomSubjectChangedHandler handler) {
	handler.onSubjectChanged(this);
    }

}
