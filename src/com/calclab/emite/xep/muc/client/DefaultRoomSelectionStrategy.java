package com.calclab.emite.xep.muc.client;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.im.client.chat.Chat;
import com.calclab.emite.im.client.chat.ChatProperties;
import com.calclab.emite.im.client.chat.ChatSelectionStrategy;

public class DefaultRoomSelectionStrategy implements ChatSelectionStrategy {

    @Override
    public ChatProperties extractChatProperties(IPacket packet) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public boolean isAssignable(Chat chat, ChatProperties properties) {
	// TODO Auto-generated method stub
	return false;
    }

}
