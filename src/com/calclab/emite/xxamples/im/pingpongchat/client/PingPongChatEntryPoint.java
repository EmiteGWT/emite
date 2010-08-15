package com.calclab.emite.xxamples.im.pingpongchat.client;

import com.calclab.emite.browser.client.PageAssist;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

public class PingPongChatEntryPoint implements EntryPoint {

    public PingPongChatEntryPoint() {
    }

    @Override
    public void onModuleLoad() {
	PingPongChatWidget output = new PingPongChatWidget();
	RootPanel.get().add(output);
	new PingPongChatPresenter(output);

	XmppURI other = XmppURI.uri(PageAssist.getMeta("pingpong.other"));
	final String clientType = PageAssist.getMeta("pingpong.type");
	if ("ping".equals(clientType)) {
	    new PingChat(other, output).start();
	} else if ("pong".equals(clientType)) {
	    new PongChat(output).start();
	} else {
	    output.print("You need to configure the client.type meta tag!!", PingPongChatDisplay.Style.error);
	}
    }

}
