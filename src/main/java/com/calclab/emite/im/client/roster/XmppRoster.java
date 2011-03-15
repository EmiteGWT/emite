package com.calclab.emite.im.client.roster;

import java.util.Collection;
import java.util.Set;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.roster.events.RosterGroupChangedHandler;
import com.calclab.emite.im.client.roster.events.RosterItemChangedHandler;
import com.calclab.emite.im.client.roster.events.RosterRetrievedHandler;
import com.google.gwt.event.shared.HandlerRegistration;

public interface XmppRoster {
    /**
     * Adds a handler to know when a roster group has changed
     * 
     * @param handler
     * @return
     */
    HandlerRegistration addRosterGroupChangedHandler(RosterGroupChangedHandler handler);

    /**
     * Adds a handler to know when a roster item has changed
     * 
     * @param handler
     * @return
     */
    HandlerRegistration addRosterItemChangedHandler(RosterItemChangedHandler handler);

    /**
     * Adds a handler to know when the roster is retrieved
     * 
     * @param handler
     * @return
     */
    HandlerRegistration addRosterRetrievedHandler(RosterRetrievedHandler handler);

    /**
     * Return the group names of this roster (null is one of the group names:
     * all the roster group)
     * 
     * @return the group names of this roster
     */
    Set<String> getGroupNames();

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
     * Get the name of this JID
     * 
     * @param jid
     *            the JID of the entity you want the name from
     * @return The item name if the entity is in the roster, or the short name
     *         of the jid
     */
    String getJidName(XmppURI jid);

    /**
     * Retrieve all the RosterItems of the Roster (a copy of the collection: can
     * be modified)
     * 
     * @return the items of the roster
     */
    Collection<RosterItem> getItems();

    /**
     * Retrieve a read-only collection with all the items that belongs to the
     * given group name.
     * 
     * @param groupName
     * @return a collection of items (this is the actual group backed. Do not
     *         modify)
     */
    Collection<RosterItem> getItemsByGroup(String groupName);

    /**
     * Return the roster group with the given name (can be null: see param)
     * 
     * @param name
     *            the name of the group. If the name is null a RosterGroup with
     *            all the items is returned
     * @return the roster group object or null if doesn't exist
     */
    RosterGroup getRosterGroup(String name);

    /**
     * Return all the groups in the roster (included the "null" named group: the
     * entired roster)
     */
    Collection<RosterGroup> getRosterGroups();

    /**
     * Checks if a valid roster has been received in the login process
     * 
     * @return true if has a roster
     */
    boolean isRosterReady();

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
     * Send a request to remove item. No listener is called until the item is
     * really removed from roster
     * 
     * @param jid
     *            the jid (resource ignored) of the roster item to be removed
     */
    void requestRemoveItem(XmppURI jid);

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
     * Request the roster again. (The roster is always retrieved when the
     * session is logged id)
     */
    void reRequestRoster();

}
