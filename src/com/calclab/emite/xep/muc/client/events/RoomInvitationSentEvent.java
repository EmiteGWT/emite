package com.calclab.emite.xep.muc.client.events;

import com.calclab.emite.core.client.events.GwtEmiteEventBus;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class RoomInvitationSentEvent extends GwtEvent<RoomInvitationSentHandler> {

    private static final Type<RoomInvitationSentHandler> TYPE = new Type<RoomInvitationSentHandler>();

    public static HandlerRegistration bind(GwtEmiteEventBus eventBus, RoomInvitationSentHandler handler) {
	return eventBus.addHandler(TYPE, handler);
    }
    private final XmppURI userJid;

    private final String reasonText;

    public RoomInvitationSentEvent(XmppURI userJid, String reasonText) {
	this.userJid = userJid;
	this.reasonText = reasonText;
    }

    @Override
    protected void dispatch(RoomInvitationSentHandler handler) {
	handler.onRoomInvitationSent(this);
    }

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<RoomInvitationSentHandler> getAssociatedType() {
	return TYPE;
    }

    public String getReasonText() {
	return reasonText;
    }

    public XmppURI getUserJid() {
	return userJid;
    }

}
