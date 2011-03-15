package com.calclab.emite.xep.muc.client.events;

import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class RoomInvitationSentEvent extends GwtEvent<RoomInvitationSentHandler> {

    private static final Type<RoomInvitationSentHandler> TYPE = new Type<RoomInvitationSentHandler>();

    public static HandlerRegistration bind(EmiteEventBus eventBus, RoomInvitationSentHandler handler) {
	return eventBus.addHandler(TYPE, handler);
    }
    private final XmppURI userJid;

    private final String reasonText;

    public RoomInvitationSentEvent(XmppURI userJid, String reasonText) {
	this.userJid = userJid;
	this.reasonText = reasonText;
    }

    @Override
    public Type<RoomInvitationSentHandler> getAssociatedType() {
	return TYPE;
    }

    public String getReasonText() {
	return reasonText;
    }

    public XmppURI getUserJid() {
	return userJid;
    }

    @Override
    protected void dispatch(RoomInvitationSentHandler handler) {
	handler.onRoomInvitationSent(this);
    }

}
