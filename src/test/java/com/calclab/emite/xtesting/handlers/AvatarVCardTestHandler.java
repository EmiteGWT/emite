package com.calclab.emite.xtesting.handlers;

import com.calclab.emite.xep.avatar.client.AvatarVCard;
import com.calclab.emite.xep.avatar.client.events.AvatarVCardHandler;
import com.calclab.emite.xep.avatar.client.events.AvatarVCardReceivedEvent;

public class AvatarVCardTestHandler extends TestHandler<AvatarVCardReceivedEvent> implements AvatarVCardHandler {

	public AvatarVCard getLastVCardResponse() {
		return hasEvent() ? getLastEvent().getAvatarVCard() : null;
	}

	@Override
	public void onAvatarVCard(AvatarVCardReceivedEvent event) {
		addEvent(event);
	}

}
