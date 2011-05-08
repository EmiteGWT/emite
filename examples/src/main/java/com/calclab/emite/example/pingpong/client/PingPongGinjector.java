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

import com.calclab.emite.browser.client.BrowserModule;
import com.calclab.emite.example.pingpong.client.logic.PingChatPresenter;
import com.calclab.emite.example.pingpong.client.logic.PingInviteRoomPresenter;
import com.calclab.emite.example.pingpong.client.logic.PingRoomPresenter;
import com.calclab.emite.example.pingpong.client.logic.PingSessionPresenter;
import com.calclab.emite.example.pingpong.client.logic.PongChatPresenter;
import com.calclab.emite.example.pingpong.client.logic.PongInviteRoomPresenter;
import com.calclab.emite.example.pingpong.client.logic.PongRoomPresenter;
import com.calclab.emite.example.pingpong.client.logic.PongSessionPresenter;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules({ PingPongModule.class, BrowserModule.class })
public interface PingPongGinjector extends Ginjector {

	PingChatPresenter getPingChatPresenter();

	PingInviteRoomPresenter getPingInviteRoomPresenter();

	PingPongCommonPresenter getPingPongCommonPresenter();

	PingPongDisplay getPingPongDisplay();

	PingRoomPresenter getPingRoomPresenter();

	PingSessionPresenter getPingSessionPresenter();

	PongChatPresenter getPongChatPresenter();

	PongInviteRoomPresenter getPongInviteRoomPresenter();

	PongRoomPresenter getPongRoomPresenter();

	PongSessionPresenter getPongSessionPresenter();

}