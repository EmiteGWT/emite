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

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;

import java.util.ArrayList;
import java.util.List;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.MatcherFactory;
import com.calclab.emite.core.client.packet.PacketMatcher;
import com.calclab.emite.core.client.xmpp.stanzas.Presence;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.core.client.xmpp.stanzas.Presence.Show;
import com.calclab.emite.core.client.xmpp.stanzas.Presence.Type;

/**
 * Represents a item (contact) in the Roster. Usually you don't create this
 * objects: you can retrieve it from the roster.
 * 
 * @see roster.getRoster();
 */
public class RosterItem {

    private static final PacketMatcher GROUP_FILTER = MatcherFactory.byName("group");

    private static Type parseAsk(final String ask) {
	Type type;
	try {
	    type = Presence.Type.valueOf(ask);
	} catch (final Exception e) {
	    type = null;
	}
	return type;
    }

    private static SubscriptionState parseSubscriptionState(final String state) {
	SubscriptionState subscriptionState;
	try {
	    subscriptionState = SubscriptionState.valueOf(state);
	} catch (final Exception e) {
	    subscriptionState = null;
	}
	return subscriptionState;
    }

    /**
     * Create a new RosterItem based on given a <item> stanza
     * 
     * @param packet
     *            the stanza
     * @return a new roster item instance
     */
    static RosterItem parse(final IPacket packet) {
	final RosterItem item = new RosterItem(uri(packet.getAttribute("jid")), parseSubscriptionState(packet
		.getAttribute("subscription")), packet.getAttribute("name"), parseAsk(packet.getAttribute("ask")));
	final List<? extends IPacket> groups = packet.getChildren(GROUP_FILTER);

	String groupName;
	for (final IPacket group : groups) {
	    groupName = group.getText();
	    item.addToGroup(groupName);
	}
	return item;
    }

    private final ArrayList<String> groups;
    private final XmppURI jid;
    private String name;
    private String status;
    private Presence.Show show;

    private SubscriptionState subscriptionState;
    private final Type ask;
    private boolean isAvailable;

    /**
     * Create a RosterItem object
     * 
     * @param jid
     *            the item jabber id
     * @param subscriptionState
     *            the subscription state
     * @param name
     *            the name in the roster
     * @param ask
     * 
     */
    public RosterItem(final XmppURI jid, final SubscriptionState subscriptionState, final String name, final Type ask) {
	this.ask = ask;
	this.jid = jid.getJID();
	this.subscriptionState = subscriptionState;
	this.name = name;
	groups = new ArrayList<String>();
	show = Show.unknown;
	isAvailable = false;
	status = null;
    }

    /**
     * Add the item to a group
     * 
     * @param group
     *            the group name to be added this item in
     */
    public void addToGroup(String group) {
	if (group != null) {
	    group = group.trim();
	    if (group.length() > 0) {
		groups.add(group);
	    }
	}
    }

    /**
     * Get the ask status of this item
     * 
     * @return the ask status
     */
    public Type getAsk() {
	return ask;
    }

    /**
     * Get the name of all the groups this items belongs to.
     * 
     * @return the list of group names
     */
    public List<String> getGroups() {
	return groups;
    }

    /**
     * Obtain the JID of the roster item
     * 
     * @see getXmppURI
     * @return the jid
     */
    public XmppURI getJID() {
	return jid;
    }

    /**
     * Get the name of the item in this roster
     * 
     * @return the name of the item
     */
    public String getName() {
	return name;
    }

    /**
     * Get the current show of this item
     * 
     * @return the show of the item
     */
    public Presence.Show getShow() {
	return show;
    }

    /**
     * Get the current status of this item
     * 
     * @return the status message
     */
    public String getStatus() {
	return status;
    }

    public SubscriptionState getSubscriptionState() {
	return subscriptionState;
    }

    /**
     * Know if a item is available or not
     * 
     * @return true if contact is available
     */
    public boolean isAvailable() {
	return isAvailable;
    }

    /**
     * Checks if the given item is in the given group
     * 
     * @param groupName
     *            the name of the group to be check
     * @return true if is included in the group, false otherwise
     */
    public boolean isInGroup(final String groupName) {
	for (final String name : groups) {
	    if (name.equals(groupName)) {
		return true;
	    }
	}
	return false;
    }

    /**
     * Removes the item from a group. This apply this in server side you have to
     * call roster.updateItem
     * 
     * @param groupName
     *            the group name to be removed from
     * @return true if removed
     * 
     * @see roster
     */
    public boolean removeFromGroup(final String groupName) {
	return groups.remove(groupName);
    }

    /**
     * Change the current available state of this item. Does NOT have any effect
     * on server side. This method is called by the roster to reflect the state
     * change of the items: usually you don't call this method.
     * 
     * @param status
     *            the new status
     */
    public void setAvailable(final boolean isAvailable) {
	this.isAvailable = isAvailable;
    }

    /**
     * Change the name of this item. To apply this change in server side you
     * MUST call roster.updateItem
     * 
     * @param name
     */
    public void setName(final String name) {
	this.name = name;
    }

    /**
     * Change the show of this item. Does NOT have any effect on server side.
     * This method is called by the roster to reflect the state change of the
     * items: usually you don't call this method.
     * 
     * @param show
     *            the new presence show state
     */
    public void setShow(final Presence.Show show) {
	this.show = show;
    }

    /**
     * Change the current status of this item. Does NOT have any effect on
     * server side. This method is called by the roster to reflect the state
     * change of the items: usually you don't call this method.
     * 
     * @param status
     *            the new status
     */
    public void setStatus(final String status) {
	this.status = status;
    }

    /**
     * Change the current substription state of this item. Does NOT have any
     * effect on server side. This method is called by the roster to reflect the
     * state change of the items: usually you don't call this method.
     * 
     * @param status
     *            the new status
     */
    public void setSubscriptionState(final SubscriptionState state) {
	subscriptionState = state;
    }

    /**
     * Creates a new <item> stanza and appends to the parent
     * 
     * @param parent
     *            the parent stanza to append the child to
     * @return the child stanza created
     */
    IPacket addStanzaTo(final IPacket parent) {
	final IPacket packet = parent.addChild("item", null);
	packet.With("jid", jid.toString()).With("name", name);
	for (final String group : groups) {
	    packet.addChild("group", null).setText(group);
	}
	return packet;
    }

    void setGroups(final String... groups) {
	this.groups.clear();
	for (final String group : groups) {
	    addToGroup(group);
	}
    }

}
