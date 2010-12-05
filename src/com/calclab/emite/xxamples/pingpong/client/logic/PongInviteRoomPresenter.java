package com.calclab.emite.xxamples.pingpong.client.logic;

import com.calclab.emite.core.client.events.StateChangedEvent;
import com.calclab.emite.core.client.events.StateChangedHandler;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.im.client.chat.Chat;
import com.calclab.emite.im.client.chat.ChatStates;
import com.calclab.emite.im.client.chat.events.ChatChangedEvent;
import com.calclab.emite.im.client.chat.events.ChatChangedHandler;
import com.calclab.emite.xep.muc.client.Room;
import com.calclab.emite.xep.muc.client.RoomInvitation;
import com.calclab.emite.xep.muc.client.RoomManager;
import com.calclab.emite.xep.muc.client.events.RoomInvitationEvent;
import com.calclab.emite.xep.muc.client.events.RoomInvitationHandler;
import com.calclab.emite.xep.muc.client.subject.RoomSubject;
import com.calclab.emite.xep.muc.client.subject.RoomSubjectChangedEvent;
import com.calclab.emite.xep.muc.client.subject.RoomSubjectChangedHandler;
import com.calclab.emite.xxamples.pingpong.client.PingPongDisplay;
import com.calclab.emite.xxamples.pingpong.client.StartablePresenter;
import com.calclab.emite.xxamples.pingpong.client.PingPongDisplay.Style;
import com.calclab.emite.xxamples.pingpong.client.events.ChatManagerEventsSupervisor;
import com.calclab.emite.xxamples.pingpong.client.events.RoomManagerEventsSupervisor;
import com.google.gwt.user.client.Timer;
import com.google.inject.Inject;

public class PongInviteRoomPresenter implements StartablePresenter {

    private final PingPongDisplay display;
    private int time;
    private int pongs;
    private final RoomManager roomManager;

    @Inject
    public PongInviteRoomPresenter(RoomManager roomManager, PingPongDisplay display) {
	this.roomManager = roomManager;
	this.display = display;
	this.time = 5000;
	this.pongs = 0;
    }

    public void start() {
	display.printHeader("This is pong invite room example", Style.title);
	display.print("You need to open the ping invite room example page", Style.important);

	new ChatManagerEventsSupervisor(roomManager, display);
	new RoomManagerEventsSupervisor(roomManager, display);

	// Accept the room invitations we receive
	roomManager.addRoomInvitationReceivedHandler(new RoomInvitationHandler() {
	    @Override
	    public void onRoomInvitation(RoomInvitationEvent event) {
		RoomInvitation invitation = event.getRoomInvitation();
		display.print("Room invitation received: " + invitation.getReason() + " - " + invitation.getInvitor()
			+ " to " + invitation.getRoomURI(), Style.important);
		display.print("We accept the invitation", Style.important);
		roomManager.acceptRoomInvitation(invitation);
	    }
	});

	// When a room is opened (by the acceptRoomInvitation method) we stay
	// for a while and then go out
	roomManager.addChatChangedHandler(new ChatChangedHandler() {
	    @Override
	    public void onChatChanged(ChatChangedEvent event) {
		if (event.isCreated()) {
		    manageNewRoom(roomManager, (Room) event.getChat());
		}
	    }
	});

    }

    private void closeRoom(final RoomManager manager, final Chat room) {
	new Timer() {
	    @Override
	    public void run() {
		display.print("We close the room: " + room.getURI(), Style.important);
		time += 2000;
		manager.close(room);
	    }

	}.schedule(time);
    }

    private void manageNewRoom(final RoomManager manager, final Room room) {
	display.print("Room created: " + room.getURI(), Style.info);
	room.addChatStateChangedHandler(true, new StateChangedHandler() {
	    @Override
	    public void onStateChanged(StateChangedEvent event) {
		if (event.is(ChatStates.ready)) {
		    display.print("We entered the room: " + room.getURI(), Style.info);
		    pongs++;
		    room.send(new Message("Pong " + pongs));
		    closeRoom(manager, room);
		}
	    }
	});

	RoomSubject.addRoomSubjectChangedHandler(room, new RoomSubjectChangedHandler() {
	    @Override
	    public void onSubjectChanged(RoomSubjectChangedEvent event) {
		display.print("Subject changed: " + event.getSubject() + "(" + event.getOccupantUri() + ")",
			Style.important);
	    }
	});

    }

}
