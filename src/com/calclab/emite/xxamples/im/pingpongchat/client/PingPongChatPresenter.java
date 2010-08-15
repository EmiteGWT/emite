package com.calclab.emite.xxamples.im.pingpongchat.client;

import com.calclab.emite.browser.client.PageAssist;
import com.calclab.emite.core.client.conn.StanzaEvent;
import com.calclab.emite.core.client.conn.StanzaHandler;
import com.calclab.emite.core.client.conn.XmppConnection;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.calclab.emite.core.client.xmpp.session.XmppSession.SessionState;
import com.calclab.emite.xxamples.im.pingpongchat.client.PingPongChatDisplay.Style;
import com.calclab.suco.client.Suco;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

public class PingPongChatPresenter {

    public PingPongChatPresenter(final PingPongChatDisplay output) {
	output.print("Welcome to ping pong example", PingPongChatDisplay.Style.title);

	final XmppSession session = Suco.get(XmppSession.class);

	output.addLoginClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		if (session.isState(SessionState.disconnected))
		    PageAssist.loginFromMeta(session);
	    }
	});

	output.addLogoutClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		if (session.isState(SessionState.loggedIn))
		    session.logout();
	    }
	});

	XmppConnection connection = Suco.get(XmppConnection.class);
	connection.addStanzaReceivedHandler(new StanzaHandler() {
	    @Override
	    public void onStanza(StanzaEvent event) {
		output.print("IN: " + event.getStanza(), Style.stanzaReceived);
	    }
	});

	connection.addStanzaSentHandler(new StanzaHandler() {
	    @Override
	    public void onStanza(StanzaEvent event) {
		output.print("OUT: " + event.getStanza(), Style.stanzaSent);
	    }
	});

    }
}
