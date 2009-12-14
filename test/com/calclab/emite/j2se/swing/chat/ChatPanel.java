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

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.calclab.suco.client.events.Event;
import com.calclab.suco.client.events.Event0;
import com.calclab.suco.client.events.Listener;
import com.calclab.suco.client.events.Listener0;

@SuppressWarnings("serial")
public class ChatPanel extends JPanel {

    private final JTextArea area;
    private final JTextField fieldBody;
    private final Event0 onClose;
    private final Event<String> onSend;
    private final JPanel sendPanel;

    public ChatPanel() {
	super(new BorderLayout());
	this.area = new JTextArea();
	add(new JScrollPane(area));

	this.onClose = new Event0("chat:onClose");
	this.onSend = new Event<String>("chat:onSend");

	final ActionListener actionListener = new ActionListener() {
	    public void actionPerformed(final ActionEvent e) {
		onSend.fire(fieldBody.getText());
	    }
	};

	fieldBody = new JTextField();
	final JButton btnSend = new JButton("send");
	fieldBody.addActionListener(actionListener);
	btnSend.addActionListener(actionListener);

	final JButton btnClose = new JButton("close");
	btnClose.addActionListener(new ActionListener() {
	    public void actionPerformed(final ActionEvent e) {
		onClose.fire();
	    }
	});

	final JPanel panel = new JPanel(new BorderLayout());
	panel.add(fieldBody);

	sendPanel = new JPanel();
	sendPanel.add(btnSend);
	sendPanel.add(btnClose);

	panel.add(sendPanel, BorderLayout.EAST);
	add(panel, BorderLayout.SOUTH);
    }

    public void clearMessage() {
	fieldBody.setText("");
	fieldBody.requestFocus();
    }

    public void onClose(final Listener0 listener) {
	onClose.add(listener);
    }

    public void onSend(final Listener<String> listener) {
	onSend.add(listener);
    }

    @Override
    public void setEnabled(final boolean enabled) {
	sendPanel.setVisible(enabled);
	super.setEnabled(enabled);
    }

    public void showIcomingMessage(final String from, final String body) {
	print(from + ": " + body);
    }

    public void showOutMessage(final String body) {
	print("me: " + body);
    }

    private void print(final String message) {
	area.setText(area.getText() + "\n" + message);
    }

}
