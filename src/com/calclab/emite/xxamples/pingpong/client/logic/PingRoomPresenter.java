package com.calclab.emite.xxamples.pingpong.client.logic;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.emite.xep.muc.client.RoomManager;
import com.calclab.emite.xxamples.pingpong.client.PingPongDisplay;
import com.calclab.emite.xxamples.pingpong.client.PingPongDisplay.Style;
import com.calclab.emite.xxamples.pingpong.client.events.RoomManagerEventsSupervisor;
import com.calclab.suco.client.Suco;

public class PingRoomPresenter extends PingChatPresenter {

    public PingRoomPresenter(XmppURI roomUri, PingPongDisplay display) {
	super(roomUri, display);
    }

    @Override
    protected ChatManager getChatManager() {
	display.printHeader("This is ping room example", Style.title);
	display.printHeader("You need to open the pong room example page in order to run the example", Style.important);
	RoomManager roomManager = Suco.get(RoomManager.class);
	new RoomManagerEventsSupervisor(roomManager, display);
	return roomManager;
    }

}
