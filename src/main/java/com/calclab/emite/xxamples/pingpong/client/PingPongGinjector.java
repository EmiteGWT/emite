package com.calclab.emite.xxamples.pingpong.client;

import com.calclab.emite.browser.client.BrowserModule;
import com.calclab.emite.xxamples.pingpong.client.logic.PingChatPresenter;
import com.calclab.emite.xxamples.pingpong.client.logic.PingInviteRoomPresenter;
import com.calclab.emite.xxamples.pingpong.client.logic.PingRoomPresenter;
import com.calclab.emite.xxamples.pingpong.client.logic.PingSessionPresenter;
import com.calclab.emite.xxamples.pingpong.client.logic.PongChatPresenter;
import com.calclab.emite.xxamples.pingpong.client.logic.PongInviteRoomPresenter;
import com.calclab.emite.xxamples.pingpong.client.logic.PongRoomPresenter;
import com.calclab.emite.xxamples.pingpong.client.logic.PongSessionPresenter;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules({PingPongModule.class, BrowserModule.class})
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