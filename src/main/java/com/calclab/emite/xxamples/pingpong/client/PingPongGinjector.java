/**
 * 
 */
package com.calclab.emite.xxamples.pingpong.client;

import com.calclab.emite.browser.client.BrowserGinjector;
import com.calclab.emite.core.client.CoreGinjector;
import com.calclab.emite.im.client.ImGinjector;
import com.calclab.emite.xep.muc.client.MucGinjector;
import com.calclab.emite.xxamples.pingpong.client.logic.PingChatPresenter;
import com.calclab.emite.xxamples.pingpong.client.logic.PingInviteRoomPresenter;
import com.calclab.emite.xxamples.pingpong.client.logic.PingRoomPresenter;
import com.calclab.emite.xxamples.pingpong.client.logic.PingSessionPresenter;
import com.calclab.emite.xxamples.pingpong.client.logic.PongChatPresenter;
import com.calclab.emite.xxamples.pingpong.client.logic.PongInviteRoomPresenter;
import com.calclab.emite.xxamples.pingpong.client.logic.PongRoomPresenter;
import com.calclab.emite.xxamples.pingpong.client.logic.PongSessionPresenter;
import com.google.gwt.inject.client.GinModules;

@GinModules(PingPongModule.class)
public interface PingPongGinjector extends CoreGinjector, ImGinjector, MucGinjector, BrowserGinjector {

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