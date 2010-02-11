package com.calclab.emite.im.client.roster;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.suco.client.events.Event;
import com.calclab.suco.client.events.Listener;

public abstract class AbstractRoster implements Roster {
    private final HashMap<XmppURI, RosterItem> itemsByJID;
    private final HashMap<String, List<RosterItem>> itemsByGroup;

    private final Event<Collection<RosterItem>> onRosterReady;
    private final Event<RosterItem> onItemAdded;
    private final Event<RosterItem> onItemChanged;
    private final Event<RosterItem> onItemRemoved;
    private boolean rosterReady;

    public AbstractRoster() {
	this.rosterReady = false;
	itemsByJID = new HashMap<XmppURI, RosterItem>();
	itemsByGroup = new HashMap<String, List<RosterItem>>();

	this.onItemAdded = new Event<RosterItem>("roster:onItemAdded");
	this.onItemChanged = new Event<RosterItem>("roster:onItemChanged");
	this.onItemRemoved = new Event<RosterItem>("roster:onItemRemoved");

	this.onRosterReady = new Event<Collection<RosterItem>>("roster:onRosterReady");
    }

    @Override
    public boolean isRosterReady() {
	return rosterReady;
    }

    @Deprecated
    public final void addItem(final XmppURI jid, final String name, final String... groups) {
	requestAddItem(jid, name, groups);
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

    protected void clearitemsByJID() {
	itemsByJID.clear();
    }

    protected void fireItemAdded(RosterItem item) {
	onItemAdded.fire(item);
    }

    protected void fireItemChanged(RosterItem item) {
	onItemChanged.fire(item);
    }

    protected void fireItemRemoved(RosterItem item) {
	onItemRemoved.fire(item);
    }

    protected void fireRosterReady(Collection<RosterItem> collection) {
	rosterReady = true;
	onRosterReady.fire(collection);
    }

    protected List<RosterItem> getGroupItems(String group) {
	return itemsByGroup.get(group);
    }

    protected Set<String> getGroupNames() {
	return itemsByGroup.keySet();
    }

    protected void putitemsByGroup(String group, List<RosterItem> items) {
	itemsByGroup.put(group, items);
    }

    protected void remove(XmppURI jid) {
	itemsByJID.remove(jid);
    }

    protected void removeFromGroup(String groupName) {
	itemsByGroup.remove(groupName);
    }

    protected void storeItem(RosterItem item) {
	itemsByJID.put(item.getJID(), item);

    }
}
