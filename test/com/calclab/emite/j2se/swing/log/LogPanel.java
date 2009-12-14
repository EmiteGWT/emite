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
package com.calclab.emite.j2se.swing.log;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.suco.client.events.Event0;
import com.calclab.suco.client.events.Listener0;

@SuppressWarnings("serial")
public class LogPanel extends JPanel {
    private final DefaultListModel model;
    private final Event0 onClear;

    public LogPanel() {
	super(new BorderLayout());

	this.onClear = new Event0("logPanel:clear");

	model = new DefaultListModel();
	add(new JScrollPane(new JList(model)), BorderLayout.CENTER);
	final JButton clear = new JButton("clear");
	clear.addActionListener(new ActionListener() {
	    public void actionPerformed(final ActionEvent e) {
		onClear.fire();
	    }
	});
	final JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	panel.add(clear);
	add(panel, BorderLayout.NORTH);
    }

    public void clear() {
	model.removeAllElements();
    }

    public void onClear(final Listener0 listener) {
	onClear.add(listener);
    }

    public void showIncomingPacket(final IPacket packet) {
	model.addElement("RECEIVED: " + packet);
    }

    public void showOutcomingPacket(final IPacket packet) {
	model.addElement("SENT: " + packet);
    }

}
