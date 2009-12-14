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
package com.calclab.emite.j2se.swing.rooms;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.calclab.suco.client.events.Event;
import com.calclab.suco.client.events.Listener;

public class RoomsPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private final Event<String> onOpenRoom;

    public RoomsPanel(final JFrame frame) {
	super(new BorderLayout());
	this.onOpenRoom = new Event<String>("roomsPanel:openRoom");
	final JPanel verticalPanel = new JPanel(new GridLayout(3, 1));
	final JTextField roomURI = new JTextField("testroom@rooms.localhost/nick");
	verticalPanel.add(roomURI);
	final JButton btnOpen = new JButton("Open room");
	btnOpen.addActionListener(new ActionListener() {
	    public void actionPerformed(final ActionEvent e) {
		onOpenRoom.fire(roomURI.getText());
	    }
	});
	verticalPanel.add(btnOpen);
	add(verticalPanel, BorderLayout.SOUTH);
    }

    public void onOpenRoom(final Listener<String> listener) {
	onOpenRoom.add(listener);
    }
}
