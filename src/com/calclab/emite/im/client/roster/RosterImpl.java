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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.MatcherFactory;
import com.calclab.emite.core.client.packet.PacketMatcher;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.stanzas.IQ;
import com.calclab.emite.core.client.xmpp.stanzas.Presence;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.core.client.xmpp.stanzas.IQ.Type;
import com.calclab.emite.core.client.xmpp.stanzas.Presence.Show;
import com.calclab.suco.client.events.Event;
import com.calclab.suco.client.events.Listener;

/**
 * @see Roster
 */
public class RosterImpl implements Roster {

    private static final PacketMatcher ROSTER_QUERY_FILTER = MatcherFactory.byNameAndXMLNS("query", "jabber:iq:roster");
    private final Session session;
    private final HashMap<XmppURI, RosterItem> itemsByJID;
    private final HashMap<String, List<RosterItem>> itemsByGroup;

    private final Event<Collection<RosterItem>> onRosterReady;
    private final Event<RosterItem> onItemAdded;
    private final Event<RosterItem> onItemChanged;
    private final Event<RosterItem> onItemRemoved;

    public RosterImpl(final Session session) {
	this.session = session;
	itemsByJID = new HashMap<XmppURI, RosterItem>();
	itemsByGroup = new HashMap<String, List<RosterItem>>();

	this.onItemAdded = new Event<RosterItem>("roster:onItemAdded");
	this.onItemChanged = new Event<RosterItem>("roster:onItemChanged");
	this.onItemRemoved = new Event<RosterItem>("roster:onItemRemoved");

	this.onRosterReady = new Event<Collection<RosterItem>>("roster:onRosterReady");

	session.onStateChanged(new Listener<Session>() {
	    @Override
	    public void onEvent(Session session) {
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
		if (type == null) {
		    item.setAvailable(true);
		} else if (type == Presence.Type.unavailable) {
		    item.setAvailable(false);
		}
		final Show showReceived = presence.getShow();
		item.setShow(showReceived == null ? Show.notSpecified : showReceived);
		item.setStatus(presence.getStatus());
		onItemChanged.fire(item);
	    }
	});

	session.onIQ(new Listener<IQ>() {
	    public void onEvent(final IQ iq) {
		if (IQ.isSet(iq)) {
		    final IPacket query = iq.getFirstChild(ROSTER_QUERY_FILTER);
		    if (query != null) {
			for (final IPacket child : query.getChildren()) {
			    handleRosterIQSet(RosterItem.parse(child));
			}
		    }
		    session.send(new IQ(Type.result).With("to", iq.getFromAsString()).With("id", iq.getId()));
		}
	    }

	});
    }

    public void addItem(final XmppURI jid, final String name, final String... groups) {
	if (getItemByJID(jid) == null) {
	    addOrUpdateItem(jid, name, null, groups);
	}
    }

    public Set<String> getGroups() {
	return itemsByGroup.keySet();
    }

    public RosterItem getItemByJID(final XmppURI jid) {
	return itemsByJID.get(jid.getJID());
    }

    public Collection<RosterItem> getItems() {
	return itemsByJID.values();
    }

    public Collection<RosterItem> getItemsByGroup(final String groupName) {
	return itemsByGroup.get(groupName);
    }

    public void onItemAdded(final Listener<RosterItem> listener) {
	onItemAdded.add(listener);
    }

    public void onItemChanged(final Listener<RosterItem> listener) {
	onItemChanged.add(listener);
    }

    public void onItemRemoved(final Listener<RosterItem> listener) {
	onItemRemoved.add(listener);
    }

    @Deprecated
    public void onItemUpdated(final Listener<RosterItem> listener) {
	onItemChanged(listener);
    }

    public void onRosterRetrieved(final Listener<Collection<RosterItem>> listener) {
	onRosterReady.add(listener);
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

    public void updateItem(final XmppURI jid, final String name, final String... groups) {
	final RosterItem oldItem = getItemByJID(jid);
	if (oldItem != null) {
	    final String newName = name == null ? oldItem.getName() : name;
	    addOrUpdateItem(jid, newName, oldItem.getSubscriptionState(), groups);
	}
    }

    /**
     * Add item either to itemsByGroup and itemsById
     * 
     * @param item
     */
    private void addItem(final RosterItem item) {
	itemsByJID.put(item.getJID(), item);
	for (final String group : item.getGroups()) {
	    List<RosterItem> items = itemsByGroup.get(group);
	    if (items == null) {
		items = new ArrayList<RosterItem>();
		itemsByGroup.put(group, items);
	    }
	    items.add(item);
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

    private void handleRosterIQSet(final RosterItem item) {
	final RosterItem old = getItemByJID(item.getJID());
	if (old == null) { // new item
	    addItem(item);
	    onItemAdded.fire(item);
	} else { // update or remove
	    removeItem(old);
	    final SubscriptionState subscriptionState = item.getSubscriptionState();
	    if (subscriptionState == SubscriptionState.remove) {
		onItemRemoved.fire(item);
	    } else {
		if (subscriptionState == SubscriptionState.to || subscriptionState == SubscriptionState.both) {
		    // already subscribed, preserve available/show/status
		    item.setAvailable(old.isAvailable());
		    item.setShow(old.getShow());
		    item.setStatus(old.getStatus());
		}
		addItem(item);
		onItemChanged.fire(item);
	    }
	}
    }

    private void removeItem(final RosterItem item) {
	itemsByJID.remove(item.getJID());
	final ArrayList<String> groupsToRemove = new ArrayList<String>();
	for (final String groupName : itemsByGroup.keySet()) {
	    final List<RosterItem> group = itemsByGroup.get(groupName);
	    group.remove(item);
	    if (group.size() == 0) {
		groupsToRemove.add(groupName);
	    }
	}
	for (final String groupName : groupsToRemove) {
	    itemsByGroup.remove(groupName);
	}
    }

    private void requestRoster(final XmppURI user) {
	session.sendIQ("roster", new IQ(IQ.Type.get, null).WithQuery("jabber:iq:roster"), new Listener<IPacket>() {
	    public void onEvent(final IPacket received) {
		if (IQ.isSuccess(received)) {
		    itemsByJID.clear();
		    final List<? extends IPacket> children = received.getFirstChild("query").getChildren();
		    for (final IPacket child : children) {
			final RosterItem item = RosterItem.parse(child);
			addItem(item);
		    }
		    onRosterReady.fire(getItems());
		}
	    }

	});
    }
}
