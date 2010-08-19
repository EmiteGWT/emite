package com.calclab.emite.xxamples.pingpong.client;

import com.calclab.emite.browser.client.PageAssist;
import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.core.client.events.EventBusFactory;
import com.calclab.emite.core.client.events.EventBusFactory.Factory;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.xxamples.pingpong.client.PingPongDisplay.Style;
import com.calclab.emite.xxamples.pingpong.client.events.ConnectionEventsSupervisor;
import com.calclab.emite.xxamples.pingpong.client.events.SessionEventsSupervisor;
import com.calclab.emite.xxamples.pingpong.client.logic.PingChatPresenter;
import com.calclab.emite.xxamples.pingpong.client.logic.PingInviteRoomPresenter;
import com.calclab.emite.xxamples.pingpong.client.logic.PingRoomPresenter;
import com.calclab.emite.xxamples.pingpong.client.logic.PingSessionPresenter;
import com.calclab.emite.xxamples.pingpong.client.logic.PongChatPresenter;
import com.calclab.emite.xxamples.pingpong.client.logic.PongInviteRoomPresenter;
import com.calclab.emite.xxamples.pingpong.client.logic.PongRoomPresenter;
import com.calclab.emite.xxamples.pingpong.client.logic.PongSessionPresenter;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

public class PingPongExamplesEntryPoint implements EntryPoint {

    @Override
    public void onModuleLoad() {
	final PingPongDisplay display = new PingPongChatWidget();

	EventBusFactory.setFactory(new Factory() {
	    @Override
	    public EmiteEventBus create(String eventBusName) {
		return new DisplayEventBus(eventBusName, display);
	    }
	});

	RootPanel.get().add(display.asWidget());
	new PingPongCommonPresenter(display);
	new ConnectionEventsSupervisor(display);
	new SessionEventsSupervisor(display);

	XmppURI other = XmppURI.uri(PageAssist.getMeta("pingpong.other"));
	XmppURI room = XmppURI.uri(PageAssist.getMeta("pingpong.room"));
	final String clientType = PageAssist.getMeta("pingpong.type");
	display.print("Ping pong example type: " + clientType, Style.info);
	if ("ping".equals(clientType)) {
	    new PingSessionPresenter(other, display).start();
	} else if ("pong".equals(clientType)) {
	    new PongSessionPresenter(other, display).start();
	} else if ("pingChat".equals(clientType)) {
	    new PingChatPresenter(other, display).start();
	} else if ("pongChat".equals(clientType)) {
	    new PongChatPresenter(display).start();
	} else if ("pingRoom".equals(clientType)) {
	    new PingRoomPresenter(room, display).start();
	} else if ("pongRoom".equals(clientType)) {
	    new PongRoomPresenter(room, display).start();
	} else if ("pingInviteRoom".equals(clientType)) {
	    new PingInviteRoomPresenter(room, display).start(other);
	} else if ("pongInviteRoom".equals(clientType)) {
	    new PongInviteRoomPresenter(display).start();
	} else {
	    display
		    .printHeader(
			    "You need to configure the pingpong.type meta tag!! "
				    + " (possible values: ping, pong, pingChat, pongChat, pingRoom, pongRoom, pingInviteRoom, pongInviteRoom)",
			    PingPongDisplay.Style.error);
	}
    }
}
