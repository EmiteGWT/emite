package com.calclab.emite.xxamples.pingpong.client.logic;

import com.calclab.emite.core.client.events.StateChangedEvent;
import com.calclab.emite.core.client.events.StateChangedHandler;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.Chat.ChatStates;
import com.calclab.emite.xep.muc.client.Room;
import com.calclab.emite.xep.muc.client.RoomManager;
import com.calclab.emite.xep.muc.client.events.OccupantChangedEvent;
import com.calclab.emite.xep.muc.client.events.OccupantChangedHandler;
import com.calclab.emite.xxamples.pingpong.client.PingPongDisplay;
import com.calclab.emite.xxamples.pingpong.client.PingPongDisplay.Style;
import com.calclab.emite.xxamples.pingpong.client.events.ChatManagerEventsSupervisor;
import com.calclab.emite.xxamples.pingpong.client.events.RoomManagerEventsSupervisor;
import com.calclab.suco.client.Suco;
import com.google.gwt.user.client.Timer;

public class PingInviteRoomPresenter {

    private final XmppURI roomUri;
    private final PingPongDisplay display;
    private int pings;
    private int time;

    public PingInviteRoomPresenter(XmppURI room, PingPongDisplay display) {
	this.roomUri = room;
	this.display = display;
	this.time = 5000;
	this.pings = 0;
    }

    public void start(final XmppURI other) {
	display.printHeader("This is ping invite room example", Style.title);
	display.printHeader("You need to open the pong invite room example page in order to run the example",
		Style.important);
	display.printHeader("Room: " + roomUri, Style.info);
	display.printHeader("Ping to: " + other, Style.info);

	RoomManager manager = Suco.get(RoomManager.class);
	new ChatManagerEventsSupervisor(manager, display);
	new RoomManagerEventsSupervisor(manager, display);

	final Room room = (Room) manager.open(roomUri);

	// When the room is ready, we invite other
	room.addChatStateChangedHandler(new StateChangedHandler() {
	    @Override
	    public void onStateChanged(StateChangedEvent event) {
		if (event.is(ChatStates.ready)) {
		    display.print("Room ready. Sending invitation to " + other, Style.info);
		    pings++;
		    room.sendInvitationTo(other, "ping invite " + pings);
		}
	    }
	}, true);

	room.addOccupantChangedHandler(new OccupantChangedHandler() {
	    @Override
	    public void onOccupantChanged(OccupantChangedEvent event) {
		if (event.isRemoved() && event.getOccupant().getURI().equalsNoResource(other)) {
		    display.print("Invited removed... waiting to send invitation", Style.info);
		    new Timer() {
			@Override
			public void run() {
			    pings++;
			    time += 1000;
			    room.sendInvitationTo(other, "ping invite " + pings);
			}
		    }.schedule(time);
		}

	    }
	});
    }
}
