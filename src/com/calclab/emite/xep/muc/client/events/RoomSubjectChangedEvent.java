package com.calclab.emite.xep.muc.client.events;

import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.xep.muc.client.Occupant;
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
    private final Occupant occupant;

    private final String subject;

    public RoomSubjectChangedEvent(Occupant occupant, String subject) {
	this.occupant = occupant;
	this.subject = subject;
    }

    @Override
    public Type<RoomSubjectChangedHandler> getAssociatedType() {
	return TYPE;
    }

    /**
     * The occupant that changes the subject
     * 
     * @return
     */
    public Occupant getOccupant() {
	return occupant;
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
