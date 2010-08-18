package com.calclab.emite.xxamples.pingpong.client.logic;

import com.calclab.emite.core.client.events.StateChangedEvent;
import com.calclab.emite.core.client.events.StateChangedHandler;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.im.client.chat.Chat;
import com.calclab.emite.im.client.chat.Chat.ChatStates;
import com.calclab.emite.im.client.chat.events.ChatChangedEvent;
import com.calclab.emite.im.client.chat.events.ChatChangedHandler;
import com.calclab.emite.xep.muc.client.RoomInvitation;
import com.calclab.emite.xep.muc.client.RoomManager;
import com.calclab.emite.xep.muc.client.events.RoomInvitationEvent;
import com.calclab.emite.xep.muc.client.events.RoomInvitationHandler;
import com.calclab.emite.xxamples.pingpong.client.PingPongDisplay;
import com.calclab.emite.xxamples.pingpong.client.PingPongDisplay.Style;
import com.calclab.emite.xxamples.pingpong.client.events.ChatManagerEventsSupervisor;
import com.calclab.emite.xxamples.pingpong.client.events.RoomManagerEventsSupervisor;
import com.calclab.suco.client.Suco;
import com.google.gwt.user.client.Timer;

public class PongInviteRoomPresenter {

    private final PingPongDisplay display;
    private int time;
    private int pongs;

    public PongInviteRoomPresenter(PingPongDisplay display) {
	this.display = display;
	this.time = 5000;
	this.pongs = 0;
    }

    public void start() {
	display.printHeader("This is ping invite room example", Style.title);
	display.printHeader("You need to open the ping invite room example page in order to run the example",
		Style.important);

	final RoomManager manager = Suco.get(RoomManager.class);
	new ChatManagerEventsSupervisor(manager, display);
	new RoomManagerEventsSupervisor(manager, display);

	// Accept the room invitations we receive
	manager.addRoomInvitationHandler(new RoomInvitationHandler() {
	    @Override
	    public void onRoomInvitation(RoomInvitationEvent event) {
		RoomInvitation invitation = event.getRoomInvitation();
		display.print("Room invitation: " + invitation.getReason() + " - " + invitation.getInvitor() + " to "
			+ invitation.getRoomURI(), Style.received);
		manager.acceptRoomInvitation(invitation);
	    }
	});

	// When a room is opened (by the acceptRoomInvitation method) we stay
	// for a while and then go out
	manager.addChatChangedHandler(new ChatChangedHandler() {
	    @Override
	    public void onChatChanged(ChatChangedEvent event) {
		final Chat room = event.getChat();
		display.print("Room created: " + room.getURI(), Style.info);
		room.addChatStateChangedHandler(new StateChangedHandler() {
		    @Override
		    public void onStateChanged(StateChangedEvent event) {
			if (event.is(ChatStates.ready)) {
			    display.print("We entered the room! " + room.getURI(), Style.info);
			    pongs++;
			    room.send(new Message("Pong " + pongs));
			    new Timer() {
				@Override
				public void run() {
				    time += 2000;
				    manager.close(room);
				}

			    }.schedule(time);
			}
		    }
		}, true);
	    }
	});
    }

}
