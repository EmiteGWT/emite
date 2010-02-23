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
import java.util.Set;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.suco.client.events.Listener;

/**
 * Implements Roster management.
 * 
 * @see http://www.xmpp.org/rfcs/rfc3921.html#roster
 */
public interface Roster {

    /**
     * renamed to requestAddItem
     */
    @Deprecated
    void addItem(XmppURI jid, String name, String... groups);

    /**
     * Return the group names of this roster
     * 
     * @return
     */
    Set<String> getGroups();

    /**
     * Find a roster item by its JID (it doesn't take care of the resource if
     * given)
     * 
     * @param jid
     *            the JID of the item (resource is ignored)
     * @return the item if found in roster, null otherwise
     */
    RosterItem getItemByJID(XmppURI jid);

    /**
     * Retrieve all the RosterItems of the Roster
     * 
     * @return the items of the roster
     */
    Collection<RosterItem> getItems();

    /**
     * Retrieve all the items that belongs to the given group name
     * 
     * @param groupName
     * @return a collection of items
     */
    Collection<RosterItem> getItemsByGroup(String groupName);

    /**
     * Checks if a valid roster has been received in the login process
     * 
     * @return true if has a roster
     */
    boolean isRosterReady();

    /**
     * Add a listener if fired when a item is added to the roster
     * 
     * @param listener
     */
    void onItemAdded(Listener<RosterItem> listener);

    /**
     * Fired when a item of the roster changed any of its attributes
     * 
     * @param listener
     */
    void onItemChanged(Listener<RosterItem> listener);

    /**
     * Add a listener to know when a item is removed from the roster
     * 
     * @param listener
     */
    void onItemRemoved(Listener<RosterItem> listener);

    /**
     * @deprecated
     * @see onItemChanged
     */
    @Deprecated
    void onItemUpdated(Listener<RosterItem> listener);

    /**
     * Add a listener to receive the Roster when ready
     * 
     * @param listener
     *            a listener that receives the roster as collection of
     *            RosterItems
     */
    void onRosterRetrieved(Listener<Collection<RosterItem>> listener);

    /**
     * Send a request to remove item. No listener is called until the item is
     * really removed from roster
     * 
     * @param jid
     *            the jid (resource ignored) of the roster item to be removed
     */
    void removeItem(XmppURI jid);

    /**
     * Request add a item to the Roster. No listener is called until the item is
     * really added to the roster. When the item is effectively added, the
     * Roster sends a subscription to the roster item's presence
     * 
     * If a item with a same JID is already present in the roster, nothing is
     * done.
     * 
     * @param jid
     *            the user JID (resource ignored)
     * @param name
     *            the item name
     * @param groups
     *            <b>(optional!)</b> the groups you want to put the groups in
     */
    void requestAddItem(XmppURI jid, String name, String... groups);

    /**
     * Request to update a item to the Roster. If the item.jid is not in the
     * roster, the item is not updated. Notice that the subscription mode is
     * IGNORED (you should use SubscriptionManager instead)
     * 
     * @param item
     *            the roster item to be updated
     * @see updateItem
     */
    void requestUpdateItem(RosterItem item);

    /**
     * Request to update the given collection of roster items. If the any of the
     * items is not previously on the roster, the item is not updated.
     * 
     * @param items
     */
    void requestUpdateItems(Collection<RosterItem> items);

    /**
     * Request to update a item to the Roster. If the item.jid is not in the
     * roster, the item is ADDED to the roster. Notice that the subscription
     * mode is IGNORED (you should use SubscriptionManager instead)
     * 
     * @param jid
     *            the roster item jid to be updated
     * @param name
     *            the new name or the old one if null
     * @param groups
     *            the new groups (ALWAYS overriden)
     */
    @Deprecated
    void updateItem(XmppURI jid, String name, String... groups);

}
