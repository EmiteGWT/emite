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
import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.core.client.events.EventBusFactory;
import com.calclab.emite.core.client.events.EventBusFactory.Factory;
import com.calclab.emite.example.pingpong.client.PingPongDisplay.Style;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class PingPongExamplesEntryPoint implements EntryPoint {

	@Override
	public void onModuleLoad() {
		final PingPongGinjector ginjector = GWT.create(PingPongGinjector.class);
		final PingPongDisplay display = ginjector.getPingPongDisplay();
		EventBusFactory.setFactory(new Factory() {
			@Override
			public EmiteEventBus create(final String eventBusName) {
				return new DisplayEventBus(eventBusName, display);
			}
		});
		ginjector.getPingPongCommonPresenter();

		RootLayoutPanel.get().add(display.asWidget());

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
			display.printHeader("You need to configure the pingpong.type meta tag!! "
					+ " (possible values: ping, pong, pingChat, pongChat, pingRoom, pongRoom, pingInviteRoom, pongInviteRoom)", PingPongDisplay.Style.error);
		}
	}
}
