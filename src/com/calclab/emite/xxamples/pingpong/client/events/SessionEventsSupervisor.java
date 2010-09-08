package com.calclab.emite.xxamples.pingpong.client.events;

import com.calclab.emite.core.client.events.StateChangedEvent;
import com.calclab.emite.core.client.events.StateChangedHandler;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.calclab.emite.core.client.xmpp.session.XmppSession.SessionStates;
import com.calclab.emite.xxamples.pingpong.client.PingPongDisplay;
import com.calclab.emite.xxamples.pingpong.client.PingPongDisplay.Style;
import com.google.gwt.user.client.Timer;
import com.google.inject.Inject;

public class SessionEventsSupervisor {

    private long currentSessionLogin;
    private int refreshTime;

    @Inject
    public SessionEventsSupervisor(final XmppSession session, final PingPongDisplay display) {
	this.currentSessionLogin = 0;
	this.refreshTime = 1000;

	// NO NEED OF LOGIN: BROWSER MODULE DOES THAT FOR US!!
	session.addSessionStateChangedHandler(true, new StateChangedHandler() {
	    @Override
	    public void onStateChanged(StateChangedEvent event) {
		display.print(("SESSION : " + event.getState()), Style.session);
		if (event.is(SessionStates.ready)) {
		    currentSessionLogin = System.currentTimeMillis();
		    display.getCurrentUser().setText("Logged as: " + session.getCurrentUser());
		    trackSessionLength(session, display);
		} else if (event.is(SessionStates.disconnected)) {
		    display.getCurrentUser().setText(" Not logged in");
		    currentSessionLogin = 0;
		    refreshTime = 1000;
		}
	    }
	});
    }

    protected void trackSessionLength(final XmppSession session, final PingPongDisplay display) {
	if (session.isReady()) {
	    long currentTime = System.currentTimeMillis();
	    int totalSeconds = (int) ((currentTime - currentSessionLogin) / 1000);
	    int minutes = totalSeconds / 60;
	    int seconds = totalSeconds % 60;
	    display.getSessionStatus().setText(
		    " Session activity for " + minutes + " minutes and " + seconds + " seconds.");
	    if (refreshTime < 10000) {
		refreshTime += 500;
	    }
	    new Timer() {
		@Override
		public void run() {
		    trackSessionLength(session, display);
		}
	    }.schedule(refreshTime);
	} else {
	    display.getSessionStatus().setText(" Session is not ready.");
	}
    }

}
