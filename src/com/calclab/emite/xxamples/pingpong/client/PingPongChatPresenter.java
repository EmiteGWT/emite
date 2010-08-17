package com.calclab.emite.xxamples.pingpong.client;

import com.calclab.emite.browser.client.PageAssist;
import com.calclab.emite.core.client.conn.StanzaEvent;
import com.calclab.emite.core.client.conn.StanzaHandler;
import com.calclab.emite.core.client.conn.XmppConnection;
import com.calclab.emite.core.client.events.StateChangedEvent;
import com.calclab.emite.core.client.events.StateChangedHandler;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.calclab.emite.core.client.xmpp.session.XmppSession.SessionStates;
import com.calclab.emite.xxamples.pingpong.client.PingPongChatDisplay.Style;
import com.calclab.suco.client.Suco;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

public class PingPongChatPresenter {

    public PingPongChatPresenter(final PingPongChatDisplay output) {
	output.printHeader("Welcome to ping pong examples", PingPongChatDisplay.Style.title);

	final XmppSession session = Suco.get(XmppSession.class);
	session.addSessionStateChangedHandler(new StateChangedHandler() {
	    @Override
	    public void onStateChanged(StateChangedEvent event) {
		if (event.is(SessionStates.loggedIn)) {
		    output.print("Logged as: " + session.getCurrentUser(), Style.info);
		}
	    }
	}, false);

	output.addLoginClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		if (session.isState(SessionStates.disconnected)) {
		    output.print("Logging in...", Style.info);
		    PageAssist.loginFromMeta(session);
		} else {
		    output.print("Current state: " + session.getSessionState(), Style.info);
		}

	    }
	});

	output.addLogoutClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		if (session.isReady()) {
		    output.print("Logging out...", Style.info);
		    session.logout();
		} else {
		    output.print("Current state: " + session.getSessionState(), Style.info);
		}

	    }
	});

	output.addClearClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		output.clearOutput();
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
