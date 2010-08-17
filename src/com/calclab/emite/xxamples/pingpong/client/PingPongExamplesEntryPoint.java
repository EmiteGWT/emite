package com.calclab.emite.xxamples.pingpong.client;

import com.calclab.emite.browser.client.PageAssist;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

public class PingPongExamplesEntryPoint implements EntryPoint {

    @Override
    public void onModuleLoad() {
	PingPongChatWidget output = new PingPongChatWidget();
	RootPanel.get().add(output);
	new PingPongChatPresenter(output);

	XmppURI other = XmppURI.uri(PageAssist.getMeta("pingpong.other"));
	final String clientType = PageAssist.getMeta("pingpong.type");
	if ("ping".equals(clientType)) {
	    new PingPresenter(other, output).start();
	} else if ("pong".equals(clientType)) {
	    new PongPresenter(other, output).start();
	} else if ("pingChat".equals(clientType)) {
	    new PingChatPresenter(other, output).start();
	} else if ("pongChat".equals(clientType)) {
	    new PongChatPresenter(output).start();
	} else if ("pingRoom".equals(clientType)) {
	} else if ("pongRoom".equals(clientType)) {

	} else {
	    output.printHeader("You need to configure the pingpong.type meta tag!! "
		    + " (possible values: ping, pong, pingChat, pongChat, pingRoom, pongRoom)",
		    PingPongChatDisplay.Style.error);
	}
    }
}
