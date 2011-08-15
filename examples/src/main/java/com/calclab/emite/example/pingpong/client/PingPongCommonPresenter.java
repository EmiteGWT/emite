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

package com.calclab.emite.example.pingpong.client;

import com.calclab.emite.browser.client.PageAssist;
import com.calclab.emite.core.client.xmpp.session.SessionStates;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.calclab.emite.example.pingpong.client.PingPongDisplay.Style;
import com.calclab.emite.example.pingpong.client.events.ConnectionEventsSupervisor;
import com.calclab.emite.example.pingpong.client.events.SessionEventsSupervisor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class PingPongCommonPresenter {

	@Inject
	public PingPongCommonPresenter(final XmppSession session, final ConnectionEventsSupervisor connectionEventsSupervisor,
			final SessionEventsSupervisor sessionEventsSupervisor, final PingPongDisplay display) {

		display.printHeader("Welcome to ping pong examples", PingPongDisplay.Style.important);

		// NO NEED OF LOGIN: BROWSER MODULE DOES THAT FOR US!!
		display.addLoginClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
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
			public void onClick(final ClickEvent event) {
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
			public void onClick(final ClickEvent event) {
				display.clearOutput();
			}
		});

	}
}
