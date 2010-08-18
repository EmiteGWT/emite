package com.calclab.emite.xxamples.pingpong.client;

import com.calclab.emite.browser.client.PageAssist;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.calclab.emite.core.client.xmpp.session.XmppSession.SessionStates;
import com.calclab.emite.xxamples.pingpong.client.PingPongDisplay.Style;
import com.calclab.suco.client.Suco;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

public class PingPongCommonPresenter {

    public PingPongCommonPresenter(final PingPongDisplay display) {
	display.printHeader("Welcome to ping pong examples", PingPongDisplay.Style.important);

	// NO NEED OF LOGIN: BROWSER MODULE DOES THAT FOR US!!
	final XmppSession session = Suco.get(XmppSession.class);

	display.addLoginClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		if (session.isState(SessionStates.disconnected)) {
		    display.print("Logging in...", Style.info);
		    PageAssist.loginFromMeta(session);
		} else {
		    display.print("Current state: " + session.getSessionState(), Style.info);
		}

	    }
	});

	display.addLogoutClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		if (session.isReady()) {
		    display.print("Logging out...", Style.info);
		    session.logout();
		} else {
		    display.print("Current state: " + session.getSessionState(), Style.info);
		}

	    }
	});

	display.addClearClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		display.clearOutput();
	    }
	});

    }
}
