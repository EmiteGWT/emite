package com.calclab.emite.xep.muc.client.events;

import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * An event to know when an invitation to a room is going to be send.
 * 
 */
public class BeforeInvitationSendEvent extends GwtEvent<BeforeInvitationSendHandler> {

    private static final Type<BeforeInvitationSendHandler> TYPE = new Type<BeforeInvitationSendHandler>();

    public static HandlerRegistration bind(EmiteEventBus eventBus, BeforeInvitationSendHandler handler) {
	return eventBus.addHandler(TYPE, handler);
    }
    private final Message message;

    private final IPacket invitePacket;

    public BeforeInvitationSendEvent(Message message, IPacket invitePacket) {
	this.message = message;
	this.invitePacket = invitePacket;
    }

    @Override
    protected void dispatch(BeforeInvitationSendHandler handler) {
	handler.onBeforeInvitationSend(this);
    }

    @Override
    public Type<BeforeInvitationSendHandler> getAssociatedType() {
	return TYPE;
    }

    public IPacket getInvitePacket() {
	return invitePacket;
    }

    public Message getMessage() {
	return message;
    }

}
