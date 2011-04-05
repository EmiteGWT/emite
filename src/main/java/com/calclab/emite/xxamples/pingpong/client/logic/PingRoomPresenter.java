package com.calclab.emite.xxamples.pingpong.client.logic;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.xep.muc.client.RoomManager;
import com.calclab.emite.xxamples.pingpong.client.PingPongDisplay;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class PingRoomPresenter extends PingChatPresenter {

    @Inject
    public PingRoomPresenter(RoomManager roomManager, @Named("room") XmppURI roomUri, PingPongDisplay display) {
	super(roomManager, roomUri, display);
    }

}
