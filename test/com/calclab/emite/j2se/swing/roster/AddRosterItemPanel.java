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
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.calclab.suco.client.events.Event0;
import com.calclab.suco.client.events.Event2;
import com.calclab.suco.client.events.Listener0;
import com.calclab.suco.client.events.Listener2;

@SuppressWarnings("serial")
public class AddRosterItemPanel extends JPanel {
    private JTextField fieldURI;
    private JTextField fieldName;
    private final Event0 onCancel;
    private final Event2<String, String> onCreate;
    private final JDialog dialog;

    public AddRosterItemPanel(final Frame owner) {
	super(new BorderLayout());
	this.onCancel = new Event0("addRoster:onCancel");
	this.onCreate = new Event2<String, String>("addRoster:onCreate");
	dialog = new JDialog(owner);
	dialog.setContentPane(this);
	dialog.setModal(true);
	dialog.pack();
	init();
    }

    public void hidePanel() {
	dialog.setVisible(false);
    }

    public void onCancel(final Listener0 listener) {
	onCancel.add(listener);
    }

    public void onCreate(final Listener2<String, String> listener2) {
	onCreate.add(listener2);
    }

    public void showPanel(final boolean b) {
	dialog.setVisible(true);
    }

    private Component createRow(final String label, final JTextField field) {
	final JPanel panel = new JPanel(new BorderLayout());
	panel.add(new JLabel(label), BorderLayout.WEST);
	panel.add(field);
	return panel;
    }

    private void init() {

	fieldURI = new JTextField();
	fieldName = new JTextField();

	final JPanel form = new JPanel(new GridLayout(2, 1));
	form.add(createRow("URI:", fieldURI));
	form.add(createRow("Name:", fieldName));
	add(form, BorderLayout.CENTER);

	final JButton btnOk = new JButton("Ok");
	btnOk.addActionListener(new ActionListener() {
	    public void actionPerformed(final ActionEvent e) {
		onCreate.fire(fieldURI.getText(), fieldName.getText());
	    }
	});

	final JButton btnCancel = new JButton("Cancel");
	btnCancel.addActionListener(new ActionListener() {
	    public void actionPerformed(final ActionEvent e) {
		onCancel.fire();
	    }
	});
	final JPanel buttons = new JPanel();
	buttons.add(btnOk);
	buttons.add(btnCancel);
	add(buttons, BorderLayout.SOUTH);
    }

}
