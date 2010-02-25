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
package com.calclab.emite.im.client.roster;

import java.util.Collection;
import java.util.List;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.MatcherFactory;
import com.calclab.emite.core.client.packet.PacketMatcher;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.stanzas.IQ;
import com.calclab.emite.core.client.xmpp.stanzas.Presence;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.core.client.xmpp.stanzas.IQ.Type;
import com.calclab.emite.core.client.xmpp.stanzas.Presence.Show;
import com.calclab.suco.client.events.Listener;
import com.google.gwt.core.client.GWT;

/**
 * Roster Xmpp implementation.
 * 
 * @see Roster
 */
public class XmppRoster extends AbstractRoster implements Roster {

    private static final PacketMatcher ROSTER_QUERY_FILTER = MatcherFactory.byNameAndXMLNS("query", "jabber:iq:roster");

    private final Session session;

    public XmppRoster(final Session session) {
	this.session = session;

	session.onStateChanged(new Listener<Session>() {
	    @Override
	    public void onEvent(final Session session) {
		if (session.getState() == Session.State.loggedIn) {
		    requestRoster(session.getCurrentUser());
		}
	    }
	});

	session.onPresence(new Listener<Presence>() {
	    public void onEvent(final Presence presence) {
		final RosterItem item = getItemByJID(presence.getFrom());
		if (item != null) {
		    setPresence(presence, item);
		}
	    }

	    private void setPresence(final Presence presence, final RosterItem item) {
		final Presence.Type type = presence.getType();
		if (type == Presence.Type.unavailable) {
		    item.setAvailable(false);
		} else {
		    item.setAvailable(true);
		}
		final Show showReceived = presence.getShow();
		item.setShow(showReceived == null ? Show.notSpecified : showReceived);
		item.setStatus(presence.getStatus());
		fireItemChanged(item);
	    }
	});

	session.onIQ(new Listener<IQ>() {
	    public void onEvent(final IQ iq) {
		if (iq.isType(IQ.Type.set)) {
		    final IPacket query = iq.getFirstChild(ROSTER_QUERY_FILTER);
		    if (query != null) {
			for (final IPacket child : query.getChildren()) {
			    handleItemChanged(RosterItem.parse(child));
			}
		    }
		    session.send(new IQ(Type.result).With("to", iq.getFromAsString()).With("id", iq.getId()));
		}
	    }

	});
    }

    public void removeItem(final XmppURI uri) {
	final RosterItem item = getItemByJID(uri.getJID());
	if (item != null) {
	    final IQ iq = new IQ(Type.set);
	    final IPacket itemNode = iq.addQuery("jabber:iq:roster").addChild("item", null);
	    itemNode.With("subscription", "remove").With("jid", item.getJID().toString());
	    session.sendIQ("remove-roster-item", iq, new Listener<IPacket>() {
		public void onEvent(final IPacket parameter) {
		}
	    });
	}
    }

    public void requestAddItem(final XmppURI jid, final String name, final String... groups) {
	if (getItemByJID(jid) == null) {
	    addOrUpdateItem(jid, name, null, groups);
	}
    }

    @Override
    public void requestUpdateItem(final RosterItem item) {
	if (getItemByJID(item.getJID()) != null) {
	    final IQ iq = new IQ(Type.set);
	    item.addStanzaTo(iq.addQuery("jabber:iq:roster"));
	    session.sendIQ("roster", iq, new Listener<IPacket>() {
		public void onEvent(final IPacket parameter) {
		}
	    });
	}
    }

    @Override
    public void requestUpdateItems(final Collection<RosterItem> items) {
	final IQ iq = new IQ(Type.set);
	final IPacket rosterQuery = iq.addQuery("jabber:iq:roster");
	for (final RosterItem item : items) {
	    item.addStanzaTo(rosterQuery);
	}
	session.sendIQ("roster", iq, new Listener<IPacket>() {
	    public void onEvent(final IPacket parameter) {
	    }
	});
    }

    public void updateItem(final XmppURI jid, final String name, final String... groups) {
	final RosterItem oldItem = getItemByJID(jid);
	if (oldItem != null) {
	    final String newName = name == null ? oldItem.getName() : name;
	    addOrUpdateItem(jid, newName, oldItem.getSubscriptionState(), groups);
	}
    }

    private void addOrUpdateItem(final XmppURI jid, final String name, final SubscriptionState subscriptionState,
	    final String... groups) {
	final RosterItem item = new RosterItem(jid, subscriptionState, name, null);
	item.setGroups(groups);
	final IQ iq = new IQ(Type.set);
	item.addStanzaTo(iq.addQuery("jabber:iq:roster"));
	session.sendIQ("roster", iq, new Listener<IPacket>() {
	    public void onEvent(final IPacket parameter) {
	    }
	});
    }

    private void handleItemChanged(final RosterItem item) {
	final RosterItem old = getItemByJID(item.getJID());
	if (old == null) { // new item
	    storeItem(item);
	    fireItemAdded(item);
	} else { // update or remove
	    removeItem(old);
	    final SubscriptionState subscriptionState = item.getSubscriptionState();
	    if (subscriptionState == SubscriptionState.remove) {
		fireItemRemoved(item);
	    } else {
		if (subscriptionState == SubscriptionState.to || subscriptionState == SubscriptionState.both) {
		    // already subscribed, preserve available/show/status
		    item.setAvailable(old.isAvailable());
		    item.setShow(old.getShow());
		    item.setStatus(old.getStatus());
		}
		storeItem(item);
		fireItemChanged(item);
	    }
	}
    }

    private void requestRoster(final XmppURI user) {
	GWT.log("Request roster");
	session.sendIQ("roster", new IQ(IQ.Type.get, null).WithQuery("jabber:iq:roster"), new Listener<IPacket>() {
	    public void onEvent(final IPacket received) {
		if (IQ.isSuccess(received)) {
		    clearGroupAll();
		    final List<? extends IPacket> children = received.getFirstChild("query").getChildren();
		    for (final IPacket child : children) {
			final RosterItem item = RosterItem.parse(child);
			storeItem(item);
		    }
		    fireRosterReady(getItems());
		}
	    }

	});
    }
}
