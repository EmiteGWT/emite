package com.calclab.emite.xtesting.handlers;

import com.calclab.emite.xep.vcard.client.VCardResponse;
import com.calclab.emite.xep.vcard.client.events.VCardResponseEvent;
import com.calclab.emite.xep.vcard.client.events.VCardResponseHandler;

public class VCardResponseTestHandler extends TestHandler<VCardResponseEvent> implements VCardResponseHandler {

	public VCardResponse getLastVCardResponse() {
		return hasEvent() ? getLastEvent().getVCardResponse() : null;
	}

	@Override
	public void onVCardResponse(VCardResponseEvent event) {
		addEvent(event);
	}

}
