/*
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008-2009 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.calclab.emite.j2se.swing.chat;

import javax.swing.JOptionPane;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.emite.im.client.chat.Chat;
import com.calclab.emite.im.client.roster.RosterItem;
import com.calclab.emite.j2se.swing.roster.RosterPanel;
import com.calclab.emite.xep.muc.client.Room;
import com.calclab.emite.xep.muc.client.RoomInvitation;
import com.calclab.emite.xep.muc.client.RoomManager;
import com.calclab.suco.client.events.Listener;

public class ConversationsControl {

    public ConversationsControl(final ChatManager chatManager, final RoomManager roomManager,
	    final RosterPanel rosterPanel, final ConversationsPanel conversationsPanel) {

	rosterPanel.onStartChat(new Listener<RosterItem>() {
	    public void onEvent(final RosterItem item) {
		chatManager.open(item.getJID());
	    }
	});

	chatManager.onChatCreated(new Listener<Chat>() {
	    public void onEvent(final Chat chat) {
		final ChatPanel chatPanel = conversationsPanel.createChat(chatManager, chat);
		chatPanel.clearMessage();
	    }
	});

	chatManager.onChatClosed(new Listener<Chat>() {
	    public void onEvent(final Chat chat) {
		conversationsPanel.close(chat.getID());
	    }
	});

	roomManager.onChatCreated(new Listener<Chat>() {
	    public void onEvent(final Chat chat) {
		final Room room = (Room) chat;
		final RoomPanel roomPanel = conversationsPanel.createRoomPanel(roomManager, room);
		roomPanel.clearMessage();
		roomPanel.showIcomingMessage(null, "The room is " + room.getState().toString());
	    }
	});

	roomManager.onChatClosed(new Listener<Chat>() {
	    public void onEvent(final Chat chat) {
		conversationsPanel.close(chat.getID());
	    }
	});

	roomManager.onInvitationReceived(new Listener<RoomInvitation>() {
	    public void onEvent(final RoomInvitation invitation) {
		final String nickName = JOptionPane
			.showInputDialog("You have been invited to a room. Please specify a nick");
		if (nickName != null) {
		    final XmppURI roomURI = invitation.getRoomURI();
		    final XmppURI jid = XmppURI.uri(roomURI.getNode(), roomURI.getHost(), nickName);
		    roomManager.open(jid);
		}
	    }
	});
    }

}
