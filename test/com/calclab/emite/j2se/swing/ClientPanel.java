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
package com.calclab.emite.j2se.swing;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import com.calclab.emite.j2se.swing.chat.ConversationsPanel;
import com.calclab.emite.j2se.swing.log.LogPanel;
import com.calclab.emite.j2se.swing.login.LoginPanel;
import com.calclab.emite.j2se.swing.rooms.RoomsPanel;
import com.calclab.emite.j2se.swing.roster.RosterPanel;

public class ClientPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private final JTabbedPane tabs;

    public ClientPanel(final LoginPanel loginPanel, final RosterPanel rosterPanel,
	    final ConversationsPanel conversationsPanel, final LogPanel logPanel, final RoomsPanel roomsPanel) {

	super(new BorderLayout());

	final JLabel status = new JLabel("emite test client");

	final JSplitPane horizontalSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
	horizontalSplit.add(conversationsPanel, JSplitPane.LEFT);
	tabs = new JTabbedPane();
	tabs.add("roster", rosterPanel);
	tabs.add("rooms", roomsPanel);
	horizontalSplit.add(tabs, JSplitPane.RIGHT);
	horizontalSplit.setDividerLocation(600);

	final JSplitPane verticalSpit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
	verticalSpit.add(horizontalSplit, JSplitPane.TOP);
	verticalSpit.add(logPanel, JSplitPane.BOTTOM);

	this.add(loginPanel, BorderLayout.NORTH);
	this.add(verticalSpit, BorderLayout.CENTER);
	this.add(status, BorderLayout.SOUTH);

    }

    public void showTabs(final boolean showTabs) {
	tabs.setVisible(showTabs);
    }

}
