package com.calclab.emite.im.client.roster;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.suco.client.events.Event;
import com.calclab.suco.client.events.Listener;

/**
 * Represents a group in a roster. All the roster itself is a group (with name
 * null)
 * 
 */
public class RosterGroup implements Iterable<RosterItem> {
    private final String name;
    private final Event<RosterItem> onItemAdded;
    private final Event<RosterItem> onItemChanged;
    private final Event<RosterItem> onItemRemoved;
    private final HashMap<XmppURI, RosterItem> itemsByJID;

    /**
     * Creates a new roster group. If name is null, its supposed to be the
     * entire roster
     * 
     * @param groupName
     *            the roster name, can be null
     * @param roster
     *            the roster object
     */
    public RosterGroup(final String groupName) {
	name = groupName;
	itemsByJID = new HashMap<XmppURI, RosterItem>();

	onItemAdded = new Event<RosterItem>("rosterGroup.onItemAdded");
	onItemChanged = new Event<RosterItem>("rosterGroup.onItemChanged");
	onItemRemoved = new Event<RosterItem>("rosterGroup.onItemRemoved");
    }

    public void add(final RosterItem item) {
	itemsByJID.put(item.getJID(), item);
	onItemAdded.fire(item);
    }

    public RosterItem getItem(final XmppURI uri) {
	return itemsByJID.get(uri.getJID());
    }

    public Collection<RosterItem> getItems() {
	return itemsByJID.values();
    }

    public String getName() {
	return name;
    }

    public int getSize() {
	return itemsByJID.size();
    }

    public boolean hasItem(final XmppURI uri) {
	return getItem(uri) != null;
    }

    public boolean isAllContacts() {
	return name == null;
    }

    @Override
    public Iterator<RosterItem> iterator() {
	return itemsByJID.values().iterator();
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

    public RosterItem remove(final XmppURI jid) {
	final RosterItem removed = itemsByJID.remove(jid);
	if (removed != null) {
	    onItemRemoved.fire(removed);
	}
	return removed;
    }

    protected void fireItemChange(final RosterItem item) {
	onItemChanged.fire(item);
    }

    void clear() {
	itemsByJID.clear();
    }

}
