package com.calclab.emite.xep.avatar.client.events;

import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.xep.avatar.client.AvatarVCard;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class AvatarVCardReceivedEvent extends GwtEvent<AvatarVCardHandler> {

    private static final Type<AvatarVCardHandler> TYPE = new Type<AvatarVCardHandler>();

    public static HandlerRegistration bind(EmiteEventBus eventBus, AvatarVCardHandler handler) {
	return eventBus.addHandler(TYPE, handler);
    }

    private final AvatarVCard avatarVCard;

    public AvatarVCardReceivedEvent(AvatarVCard avatarVCard) {
	this.avatarVCard = avatarVCard;
    }

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<AvatarVCardHandler> getAssociatedType() {
	return TYPE;
    }

    public AvatarVCard getAvatarVCard() {
	return avatarVCard;
    }

    @Override
    protected void dispatch(AvatarVCardHandler handler) {
	handler.onAvatarVCard(this);
    }

}
