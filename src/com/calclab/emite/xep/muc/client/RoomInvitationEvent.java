package com.calclab.emite.xep.muc.client;

import com.calclab.emite.core.client.events.EmiteEventBus;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class RoomInvitationEvent extends GwtEvent<RoomInvitationHandler> {
    private static final Type<RoomInvitationHandler> TYPE = new Type<RoomInvitationHandler>();

    public static HandlerRegistration bind(EmiteEventBus eventBus, RoomInvitationHandler handler) {
	return eventBus.addHandler(TYPE, handler);
    }
    private final RoomInvitation roomInvitation;

    public RoomInvitationEvent(RoomInvitation roomInvitation) {
	this.roomInvitation = roomInvitation;
    }

    @Override
    protected void dispatch(RoomInvitationHandler handler) {
	handler.onRoomInvitation(this);
    }

    @Override
    public Type<RoomInvitationHandler> getAssociatedType() {
	return TYPE;
    }

    public RoomInvitation getRoomInvitation() {
	return roomInvitation;
    }

}
