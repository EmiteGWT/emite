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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.calclab.emite.xep.muc.client.Occupant;
import com.calclab.suco.client.events.Event;
import com.calclab.suco.client.events.Event2;
import com.calclab.suco.client.events.Listener;
import com.calclab.suco.client.events.Listener2;

@SuppressWarnings("serial")
public class RoomPanel extends ChatPanel {

    private final DefaultListModel users;
    private final JList usersList;
    private final JTextField fieldSubjRoom;
    private final JTextField fieldInvitedUser;
    private final Event2<String, String> onInviteUser;
    private final Event<String> onSubjectModified;

    public RoomPanel() {
	super();
	this.onInviteUser = new Event2<String, String>("roomPanel:InviteUser");
	this.onSubjectModified = new Event<String>("roomPanel:onSubjectModified");

	users = new DefaultListModel();
	usersList = new JList(users);
	add(usersList, BorderLayout.EAST);

	final JButton btnModSubject = new JButton("modify subject");
	btnModSubject.addActionListener(new ActionListener() {
	    public void actionPerformed(final ActionEvent e) {
		onSubjectModified.fire(fieldSubjRoom.getText());
	    }
	});
	final JButton btnInvite = new JButton("invite user");
	btnInvite.addActionListener(new ActionListener() {
	    public void actionPerformed(final ActionEvent e) {
		onInviteUser.fire(fieldInvitedUser.getText(), "This is the reason");
	    }
	});
	fieldSubjRoom = new JTextField();
	fieldSubjRoom.setText("Write here the room subject");
	fieldInvitedUser = new JTextField();
	fieldInvitedUser.setText("testuser1@localhost");

	final JPanel northPanel = new JPanel();
	northPanel.add(fieldSubjRoom);
	northPanel.add(btnModSubject);
	northPanel.add(fieldInvitedUser);
	northPanel.add(btnInvite);
	add(northPanel, BorderLayout.NORTH);
    }

    public void onSubjectChanged(final Listener<String> listener) {
	onSubjectModified.add(listener);
    }

    public void onUserInvited(final Listener2<String, String> listener) {
	onInviteUser.add(listener);

    }

    public void setUsers(final Collection<Occupant> users) {
	this.users.clear();
	for (final Occupant user : users) {
	    this.users.addElement(user);
	}
    }

    @Override
    public void showIcomingMessage(final String from, final String body) {
	String name;
	if (from == null) {
	    name = "";
	} else {
	    name = from.substring(from.indexOf('/') + 1);
	}
	super.showIcomingMessage(name, body);
    }

    @Override
    public void showOutMessage(final String body) {
    }

}
