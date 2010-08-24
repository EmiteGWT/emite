package com.calclab.emite.xxamples.pingpong.client.events;

import com.calclab.emite.core.client.events.PresenceEvent;
import com.calclab.emite.core.client.events.PresenceHandler;
import com.calclab.emite.core.client.events.ChangedEvent.ChangeTypes;
import com.calclab.emite.im.client.chat.events.ChatChangedEvent;
import com.calclab.emite.im.client.chat.events.ChatChangedHandler;
import com.calclab.emite.xep.muc.client.Occupant;
import com.calclab.emite.xep.muc.client.Room;
import com.calclab.emite.xep.muc.client.RoomManager;
import com.calclab.emite.xep.muc.client.events.OccupantChangedEvent;
import com.calclab.emite.xep.muc.client.events.OccupantChangedHandler;
import com.calclab.emite.xxamples.pingpong.client.PingPongDisplay;
import com.calclab.emite.xxamples.pingpong.client.PingPongDisplay.Style;

public class RoomManagerEventsSupervisor {

    public RoomManagerEventsSupervisor(RoomManager roomManager, final PingPongDisplay display) {
	roomManager.addChatChangedHandler(new ChatChangedHandler() {
	    @Override
	    public void onChatChanged(ChatChangedEvent event) {
		if (event.is(ChangeTypes.created)) {
		    trackRoom((Room) event.getChat(), display);
		}
	    }
	});
    }

    protected void trackRoom(final Room room, final PingPongDisplay display) {
	room.addOccupantChangedHandler(new OccupantChangedHandler() {
	    @Override
	    public void onOccupantChanged(OccupantChangedEvent event) {
		display.print("ROOM OCCUPANT " + event.getOccupant().getNick() + " changed: " + event.getChangeType(),
			Style.event);
		String occupants = "";
		for (Occupant occupant : room.getOccupants()) {
		    occupants += occupant.getOccupantUri().getResource() + " ";
		}
		display.print("ROOM OCCUPANTS (" + room.getOccupantsCount() + "): " + occupants, Style.event);
	    }
	});

	room.addPresenceReceivedHandler(new PresenceHandler() {
	    @Override
	    public void onPresence(PresenceEvent event) {
		display.print("ROOM PRESENCE : " + event.getPresence(), Style.event);
	    }
	});
    }

}
