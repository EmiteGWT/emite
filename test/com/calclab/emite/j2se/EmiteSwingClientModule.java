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
package com.calclab.emite.j2se;

import javax.swing.JFrame;

import com.calclab.emite.core.client.EmiteCoreModule;
import com.calclab.emite.core.client.bosh.Connection;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.im.client.InstantMessagingModule;
import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.emite.im.client.roster.Roster;
import com.calclab.emite.im.client.roster.SubscriptionManager;
import com.calclab.emite.j2se.services.J2SEServicesModule;
import com.calclab.emite.j2se.swing.ClientControl;
import com.calclab.emite.j2se.swing.ClientPanel;
import com.calclab.emite.j2se.swing.FrameControl;
import com.calclab.emite.j2se.swing.chat.ConversationsControl;
import com.calclab.emite.j2se.swing.chat.ConversationsPanel;
import com.calclab.emite.j2se.swing.log.LogControl;
import com.calclab.emite.j2se.swing.log.LogPanel;
import com.calclab.emite.j2se.swing.login.LoginControl;
import com.calclab.emite.j2se.swing.login.LoginPanel;
import com.calclab.emite.j2se.swing.rooms.RoomsControl;
import com.calclab.emite.j2se.swing.rooms.RoomsPanel;
import com.calclab.emite.j2se.swing.roster.AddRosterItemPanel;
import com.calclab.emite.j2se.swing.roster.RosterControl;
import com.calclab.emite.j2se.swing.roster.RosterPanel;
import com.calclab.emite.xep.muc.client.MUCModule;
import com.calclab.emite.xep.muc.client.RoomManager;
import com.calclab.suco.client.Suco;
import com.calclab.suco.client.ioc.decorator.Singleton;
import com.calclab.suco.client.ioc.module.AbstractModule;
import com.calclab.suco.client.ioc.module.Factory;

public class EmiteSwingClientModule extends AbstractModule {

    public static void main(final String args[]) {
	Suco.install(new EmiteCoreModule(), new J2SEServicesModule(), new InstantMessagingModule(), new MUCModule(),
		new EmiteSwingClientModule());
	Suco.get(JFrame.class).setVisible(true);
    }

    public EmiteSwingClientModule() {
	super();
    }

    @Override
    protected void onInstall() {
	register(Singleton.class, new Factory<ClientPanel>(ClientPanel.class) {
	    @Override
	    public ClientPanel create() {
		return new ClientPanel($(LoginPanel.class), $(RosterPanel.class), $(ConversationsPanel.class),
			$(LogPanel.class), $(RoomsPanel.class));
	    }

	    @Override
	    public void onAfterCreated(final ClientPanel instance) {
		new ClientControl($(Session.class), instance);
	    }
	});

	register(Singleton.class, new Factory<JFrame>(JFrame.class) {
	    @Override
	    public JFrame create() {
		return new JFrame("emite swing client");
	    }

	    @Override
	    public void onAfterCreated(final JFrame instance) {
		new FrameControl($(Session.class), $(ClientPanel.class), instance);
	    }
	});

	register(Singleton.class, new Factory<LogPanel>(LogPanel.class) {
	    @Override
	    public LogPanel create() {
		return new LogPanel();
	    }

	    @Override
	    public void onAfterCreated(final LogPanel instance) {
		new LogControl($(Connection.class), instance);
	    }

	}, new Factory<LoginPanel>(LoginPanel.class) {
	    @Override
	    public LoginPanel create() {
		return new LoginPanel($(JFrame.class));
	    }

	    @Override
	    public void onAfterCreated(final LoginPanel instance) {
		new LoginControl($(Connection.class), $(Session.class), instance);
	    }

	}, new Factory<AddRosterItemPanel>(AddRosterItemPanel.class) {
	    @Override
	    public AddRosterItemPanel create() {
		final AddRosterItemPanel panel = new AddRosterItemPanel($(JFrame.class));
		return panel;
	    }
	}, new Factory<RosterPanel>(RosterPanel.class) {
	    @Override
	    public RosterPanel create() {
		final RosterPanel panel = new RosterPanel($(JFrame.class), $(AddRosterItemPanel.class));
		return panel;
	    }

	    @Override
	    public void onAfterCreated(final RosterPanel instance) {
		new RosterControl($(Session.class), $(Roster.class), $(SubscriptionManager.class), instance);
	    }

	}, new Factory<RoomsPanel>(RoomsPanel.class) {
	    @Override
	    public RoomsPanel create() {
		final RoomsPanel panel = new RoomsPanel($(JFrame.class));
		return panel;
	    }

	    @Override
	    public void onAfterCreated(final RoomsPanel instance) {
		new RoomsControl($(Session.class), instance, $(RoomManager.class));
	    }

	}, new Factory<ConversationsPanel>(ConversationsPanel.class) {
	    @Override
	    public ConversationsPanel create() {
		return new ConversationsPanel();
	    }

	    @Override
	    public void onAfterCreated(final ConversationsPanel instance) {
		new ConversationsControl($(ChatManager.class), $(RoomManager.class), $(RosterPanel.class), instance);
	    }
	});
    }
}
