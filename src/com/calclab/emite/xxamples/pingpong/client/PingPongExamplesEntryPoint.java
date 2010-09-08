package com.calclab.emite.xxamples.pingpong.client;

import com.calclab.emite.browser.client.PageAssist;
import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.core.client.events.EventBusFactory;
import com.calclab.emite.core.client.events.EventBusFactory.Factory;
import com.calclab.emite.xxamples.pingpong.client.PingPongDisplay.Style;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;

public class PingPongExamplesEntryPoint implements EntryPoint {

    @Override
    public void onModuleLoad() {
	final PingPongGinjector ginjector = GWT.create(PingPongGinjector.class);
	final PingPongDisplay display = ginjector.getPingPongDisplay();
	EventBusFactory.setFactory(new Factory() {
	    @Override
	    public EmiteEventBus create(String eventBusName) {
		return new DisplayEventBus(eventBusName, display);
	    }
	});
	ginjector.getPingPongCommonPresenter();

	RootPanel.get().add(display.asWidget());

	final String clientType = PageAssist.getMeta("pingpong.type");
	display.print("Ping pong example type: " + clientType, Style.info);
	StartablePresenter presenter = null;
	if ("ping".equals(clientType)) {
	    presenter = ginjector.getPingSessionPresenter();
	} else if ("pong".equals(clientType)) {
	    presenter = ginjector.getPongSessionPresenter();
	} else if ("pingChat".equals(clientType)) {
	    presenter = ginjector.getPingChatPresenter();
	} else if ("pongChat".equals(clientType)) {
	    presenter = ginjector.getPongChatPresenter();
	} else if ("pingRoom".equals(clientType)) {
	    presenter = ginjector.getPingRoomPresenter();
	} else if ("pongRoom".equals(clientType)) {
	    presenter = ginjector.getPongRoomPresenter();
	} else if ("pingInviteRoom".equals(clientType)) {
	    presenter = ginjector.getPingInviteRoomPresenter();
	} else if ("pongInviteRoom".equals(clientType)) {
	    presenter = ginjector.getPongInviteRoomPresenter();
	}

	if (presenter != null) {
	    presenter.start();
	} else {

	    display
		    .printHeader(
			    "You need to configure the pingpong.type meta tag!! "
				    + " (possible values: ping, pong, pingChat, pongChat, pingRoom, pongRoom, pingInviteRoom, pongInviteRoom)",
			    PingPongDisplay.Style.error);
	}
    }
}
