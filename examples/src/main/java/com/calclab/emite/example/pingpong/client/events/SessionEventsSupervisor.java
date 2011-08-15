/*
 * ((e)) emite: A pure Google Web Toolkit XMPP library
 * Copyright (c) 2008-2011 The Emite development team
 * 
 * This file is part of Emite.
 *
 * Emite is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * Emite is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with Emite.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.calclab.emite.example.pingpong.client.events;

import com.calclab.emite.core.client.events.StateChangedEvent;
import com.calclab.emite.core.client.events.StateChangedHandler;
import com.calclab.emite.core.client.xmpp.session.SessionStates;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.calclab.emite.example.pingpong.client.PingPongDisplay;
import com.calclab.emite.example.pingpong.client.PingPongDisplay.Style;
import com.google.gwt.user.client.Timer;
import com.google.inject.Inject;

public class SessionEventsSupervisor {

	private long currentSessionLogin;
	private int refreshTime;

	@Inject
	public SessionEventsSupervisor(final XmppSession session, final PingPongDisplay display) {
		currentSessionLogin = 0;
		refreshTime = 1000;

		// NO NEED OF LOGIN: BROWSER MODULE DOES THAT FOR US!!
		session.addSessionStateChangedHandler(true, new StateChangedHandler() {
			@Override
			public void onStateChanged(final StateChangedEvent event) {
				display.print(("SESSION : " + event.getState()), Style.session);
				if (event.is(SessionStates.ready)) {
					currentSessionLogin = System.currentTimeMillis();
					display.getCurrentUser().setText("Logged as: " + session.getCurrentUserURI());
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
			final long currentTime = System.currentTimeMillis();
			final int totalSeconds = (int) ((currentTime - currentSessionLogin) / 1000);
			final int minutes = totalSeconds / 60;
			final int seconds = totalSeconds % 60;
			display.getSessionStatus().setText(" Session activity for " + minutes + " minutes and " + seconds + " seconds.");
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
