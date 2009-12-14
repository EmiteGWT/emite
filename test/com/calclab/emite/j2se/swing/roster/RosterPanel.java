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
package com.calclab.emite.j2se.swing.roster;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.calclab.emite.im.client.roster.RosterItem;
import com.calclab.suco.client.events.Event;
import com.calclab.suco.client.events.Event2;
import com.calclab.suco.client.events.Listener;
import com.calclab.suco.client.events.Listener0;
import com.calclab.suco.client.events.Listener2;

@SuppressWarnings("serial")
public class RosterPanel extends JPanel {

    private JList list;
    private DefaultListModel model;
    private final Event2<String, String> onAddRosterItem;
    private final Event<RosterItem> onRemoveItem;
    private final Event<RosterItem> onStartChat;
    private final JFrame frame;
    private final AddRosterItemPanel addRosterPanel;

    public RosterPanel(final JFrame frame, final AddRosterItemPanel addRosterPanel) {
	super(new BorderLayout());
	this.frame = frame;
	this.addRosterPanel = addRosterPanel;
	this.onAddRosterItem = new Event2<String, String>("roster:onAddRosterItem");
	this.onRemoveItem = new Event<RosterItem>("roster:onRemoveItem");
	this.onStartChat = new Event<RosterItem>("roster:onStartChat");
	init();
	initAddPanel();
    }

    public void addItem(final String name, final RosterItem item) {
	model.addElement(new RosterItemWrapper(name, item));
    }

    public void clear() {
	model.clear();
    }

    public boolean isConfirmed(final String message) {
	final int result = JOptionPane.showConfirmDialog(frame, message);
	return (result == JOptionPane.OK_OPTION);
    }

    public void onAddRosterItem(final Listener2<String, String> listener) {
	onAddRosterItem.add(listener);
    }

    public void onRemoveItem(final Listener<RosterItem> listener) {
	onRemoveItem.add(listener);
    }

    public void onStartChat(final Listener<RosterItem> listener) {
	onStartChat.add(listener);
    }

    public void refresh() {
	list.repaint();
    }

    private void init() {
	model = new DefaultListModel();
	list = new JList(model);
	list.addListSelectionListener(new ListSelectionListener() {
	    public void valueChanged(final ListSelectionEvent e) {
	    }
	});
	this.add(new JScrollPane(list), BorderLayout.CENTER);
	final JButton btnChat = new JButton("chat");
	btnChat.addActionListener(new ActionListener() {
	    public void actionPerformed(final ActionEvent e) {
		final Object value = list.getSelectedValue();
		if (value != null) {
		    final RosterItemWrapper wrapper = (RosterItemWrapper) value;
		    onStartChat.fire(wrapper.item);
		}
	    }
	});

	final JButton btnAddContact = new JButton("add contact");

	btnAddContact.addActionListener(new ActionListener() {
	    public void actionPerformed(final ActionEvent e) {
		addRosterPanel.showPanel(true);
	    }
	});

	final JButton btnRemoveContact = new JButton("remove contact");
	btnRemoveContact.addActionListener(new ActionListener() {
	    public void actionPerformed(final ActionEvent e) {
		final RosterItemWrapper wrapper = (RosterItemWrapper) list.getSelectedValue();
		if (wrapper != null) {
		    onRemoveItem.fire(wrapper.item);
		}
	    }
	});

	final JPanel buttons = new JPanel(new GridLayout(3, 1));
	buttons.add(btnChat);
	buttons.add(btnAddContact);
	buttons.add(btnRemoveContact);
	add(buttons, BorderLayout.SOUTH);
    }

    private void initAddPanel() {
	addRosterPanel.onCancel(new Listener0() {
	    public void onEvent() {
		addRosterPanel.hidePanel();
	    }
	});

	addRosterPanel.onCreate(new Listener2<String, String>() {
	    public void onEvent(final String jid, final String name) {
		addRosterPanel.hidePanel();
		onAddRosterItem.fire(jid, name);
	    }

	});
    }

}
