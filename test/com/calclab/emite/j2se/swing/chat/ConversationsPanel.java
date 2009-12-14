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

import java.awt.BorderLayout;
import java.util.HashMap;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.emite.im.client.chat.Chat;
import com.calclab.emite.xep.muc.client.Room;
import com.calclab.emite.xep.muc.client.RoomManager;

@SuppressWarnings("serial")
public class ConversationsPanel extends JPanel {
    private final JTabbedPane tabs;
    private final HashMap<String, ChatPanel> panels;

    public ConversationsPanel() {
	super(new BorderLayout());
	this.tabs = new JTabbedPane();
	this.add(new JScrollPane(tabs));
	this.panels = new HashMap<String, ChatPanel>();
    }

    public void close(final String id) {
	tabs.remove(panels.get(id));
    }

    public ChatPanel createChat(final ChatManager chatManager, final Chat chat) {
	final ChatPanel panel = new ChatPanel();
	new ChatControl(chatManager, chat, panel);
	return addChat(chat.getURI().toString(), chat.getID(), panel);
    }

    public RoomPanel createRoomPanel(final RoomManager roomManager, final Room room) {

	final RoomPanel panel = new RoomPanel();
	new RoomControl(roomManager, room, panel);
	return (RoomPanel) addChat(room.getURI().toString(), room.getID(), panel);
    }

    private ChatPanel addChat(final String title, final String id, final ChatPanel panel) {
	panels.put(id, panel);
	tabs.addTab(title, panel);
	return panel;
    }
}
