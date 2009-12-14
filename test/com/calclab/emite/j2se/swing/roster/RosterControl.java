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

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;

import java.util.Collection;

import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.roster.Roster;
import com.calclab.emite.im.client.roster.RosterItem;
import com.calclab.emite.im.client.roster.SubscriptionManager;
import com.calclab.suco.client.events.Listener;
import com.calclab.suco.client.events.Listener2;

public class RosterControl {

    public RosterControl(final Session session, final Roster roster, final SubscriptionManager subscriptionManager,
	    final RosterPanel rosterPanel) {

	rosterPanel.setEnabled(false);

	rosterPanel.onAddRosterItem(new Listener2<String, String>() {
	    public void onEvent(final String jid, final String name) {
		roster.addItem(uri(jid), name);
	    }
	});

	rosterPanel.onRemoveItem(new Listener<RosterItem>() {
	    public void onEvent(final RosterItem item) {
		roster.removeItem(item.getJID());
	    }
	});

	roster.onRosterRetrieved(new Listener<Collection<RosterItem>>() {
	    public void onEvent(final Collection<RosterItem> items) {
		rosterPanel.clear();
		for (final RosterItem item : items) {
		    rosterPanel.addItem(item.getName(), item);
		}
	    }
	});

	roster.onItemAdded(new Listener<RosterItem>() {
	    public void onEvent(final RosterItem item) {
		rosterPanel.addItem(item.getName(), item);
	    }
	});

	roster.onItemChanged(new Listener<RosterItem>() {
	    public void onEvent(final RosterItem parameter) {
		rosterPanel.refresh();
	    }
	});

	roster.onItemRemoved(new Listener<RosterItem>() {
	    public void onEvent(final RosterItem parameter) {
	    }
	});

	session.onStateChanged(new Listener<Session.State>() {
	    public void onEvent(final Session.State current) {
		rosterPanel.setEnabled(current == Session.State.ready);
		if (current == Session.State.disconnected) {
		    rosterPanel.clear();
		}
	    }
	});

	subscriptionManager.onSubscriptionRequested(new Listener2<XmppURI, String>() {
	    public void onEvent(final XmppURI uri, final String nick) {
		final String message = uri.toString() + " want to add you to his/her roster. Accept?";
		if (rosterPanel.isConfirmed(message)) {
		    subscriptionManager.approveSubscriptionRequest(uri, nick);
		} else {
		    subscriptionManager.refuseSubscriptionRequest(uri);
		}
	    }
	});

    }
}
