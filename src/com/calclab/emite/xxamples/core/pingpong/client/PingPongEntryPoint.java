package com.calclab.emite.xxamples.core.pingpong.client;

import com.calclab.emite.browser.client.PageAssist;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.suco.client.Suco;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PingPongEntryPoint implements EntryPoint {

    public static interface Output {

	public static class Style {
	    public static final String title = "title";
	    public static final String error = "error";
	    public static final String important = "important";
	    protected static final String received = "received";
	    protected static final String session = "session";
	    public static final String info = "info";
	    public static final String sent = "sent";
	}

	public void print(String message, String style);

    }

    private VerticalPanel panel;
    private final int pings;
    private final int pongs;
    private final XmppSession session;
    private XmppURI other;

    public PingPongEntryPoint() {
	pings = 0;
	pongs = 0;
	session = Suco.get(XmppSession.class);
    }

    @Override
    public void onModuleLoad() {
	panel = new VerticalPanel();
	RootPanel.get().add(panel);

	final Output output = new Output() {
	    @Override
	    public void print(final String text, final String style) {
		final Label label = new Label(text);
		label.addStyleName(style);
		panel.insert(label, 0);
	    }

	};

	output.print("Welcome to ping pong example", Output.Style.title);

	other = XmppURI.uri(PageAssist.getMeta("pingpong.other"));
	final String clientType = PageAssist.getMeta("pingpong.type");
	if ("ping".equals(clientType)) {
	    new Ping(other, output).start();
	} else if ("pong".equals(clientType)) {
	    new Pong(other, output).start();
	} else {
	    output.print("You need to configure the client.type meta tag!!", Output.Style.error);
	}
    }

}
