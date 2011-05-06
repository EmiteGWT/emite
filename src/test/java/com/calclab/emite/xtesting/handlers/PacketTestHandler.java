package com.calclab.emite.xtesting.handlers;

import com.calclab.emite.core.client.events.PacketEvent;
import com.calclab.emite.core.client.events.PacketHandler;

public class PacketTestHandler extends TestHandler<PacketEvent> implements PacketHandler {

	@Override
	public void onPacket(final PacketEvent event) {
		addEvent(event);
	}

}
