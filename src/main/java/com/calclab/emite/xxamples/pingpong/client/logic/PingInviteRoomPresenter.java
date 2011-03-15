package com.calclab.emite.xxamples.pingpong.client.logic;

import com.calclab.emite.core.client.events.StateChangedEvent;
import com.calclab.emite.core.client.events.StateChangedHandler;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.ChatStates;
import com.calclab.emite.xep.muc.client.Room;
import com.calclab.emite.xep.muc.client.RoomManager;
import com.calclab.emite.xep.muc.client.events.OccupantChangedEvent;
import com.calclab.emite.xep.muc.client.events.OccupantChangedHandler;
import com.calclab.emite.xep.muc.client.subject.RoomSubject;
import com.calclab.emite.xxamples.pingpong.client.PingPongDisplay;
import com.calclab.emite.xxamples.pingpong.client.StartablePresenter;
import com.calclab.emite.xxamples.pingpong.client.PingPongDisplay.Style;
import com.calclab.emite.xxamples.pingpong.client.events.ChatManagerEventsSupervisor;
import com.calclab.emite.xxamples.pingpong.client.events.RoomManagerEventsSupervisor;
import com.google.gwt.user.client.Timer;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class PingInviteRoomPresenter implements StartablePresenter {

    private final XmppURI roomUri;
    private final PingPongDisplay display;
    private int pings;
    private int time;
    private final XmppURI otherUri;
    private final RoomManager roomManager;

    @Inject
    public PingInviteRoomPresenter(RoomManager roomManager, @Named("other") XmppURI other, @Named("room") XmppURI room,
	    PingPongDisplay display) {
	this.roomManager = roomManager;
	this.otherUri = other;
	this.roomUri = room;
	this.display = display;
	this.time = 5000;
	this.pings = 0;
    }

    public void start() {
	display.printHeader("This is ping invite room example", Style.title);
	display.printHeader("You need to open the pong invite room example page in order to run the example",
		Style.important);
	display.printHeader("Room: " + roomUri, Style.info);
	display.printHeader("Ping to: " + otherUri, Style.info);

	new ChatManagerEventsSupervisor(roomManager, display);
	new RoomManagerEventsSupervisor(roomManager, display);

	final Room room = (Room) roomManager.open(roomUri);

	// When the room is ready, we invite other
	room.addChatStateChangedHandler(true, new StateChangedHandler() {
	    @Override
	    public void onStateChanged(StateChangedEvent event) {
		if (event.is(ChatStates.ready)) {
		    display.print("Room ready. Sending invitation to " + otherUri, Style.important);
		    pings++;
		    room.sendInvitationTo(otherUri, "ping invite " + pings);
		}
	    }
	});

	room.addOccupantChangedHandler(new OccupantChangedHandler() {
	    @Override
	    public void onOccupantChanged(OccupantChangedEvent event) {
		boolean isOtherOccupant = event.getOccupant().getUserUri().equalsNoResource(otherUri);
		if (isOtherOccupant) {
		    if (event.isRemoved()) {
			display.print("Invited removed... waiting to send invitation", Style.important);
			new Timer() {
			    @Override
			    public void run() {
				display.print("Sending invitation", Style.important);
				pings++;
				time += 1000;
				room.sendInvitationTo(otherUri, "ping invite " + pings);
			    }
			}.schedule(time);
		    } else if (event.isAdded()) {
			display.print("Change subject", Style.important);
			RoomSubject.requestSubjectChange(room, "Subject ping" + pings);
		    }
		}
	    }
	});

    }
}
