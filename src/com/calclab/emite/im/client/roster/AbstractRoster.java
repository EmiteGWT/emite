package com.calclab.emite.im.client.roster;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.suco.client.events.Event;
import com.calclab.suco.client.events.Listener;

/**
 * Implements all roster method not related directly with xmpp: boileplate code
 * and group handling code
 * 
 */
public abstract class AbstractRoster implements Roster {

    private final HashMap<String, RosterGroup> groups;

    private final Event<Collection<RosterItem>> onRosterReady;
    private final Event<RosterItem> onItemAdded;
    private final Event<RosterItem> onItemChanged;
    private final Event<RosterItem> onItemRemoved;
    private final Event<RosterGroup> onGroupRemoved;
    private final Event<RosterGroup> onGroupAdded;

    private boolean rosterReady;

    private final RosterGroup all;

    public AbstractRoster() {
	rosterReady = false;
	groups = new HashMap<String, RosterGroup>();

	onItemAdded = new Event<RosterItem>("roster:onItemAdded");
	onItemChanged = new Event<RosterItem>("roster:onItemChanged");
	onItemRemoved = new Event<RosterItem>("roster:onItemRemoved");
	onGroupRemoved = new Event<RosterGroup>("roster.onGroupRemoved");
	onGroupAdded = new Event<RosterGroup>("roster.onGroupAdded");
	onRosterReady = new Event<Collection<RosterItem>>("roster:onRosterReady");

	all = new RosterGroup(null);
    }

    @Deprecated
    public final void addItem(final XmppURI jid, final String name, final String... groups) {
	requestAddItem(jid, name, groups);
    }

    public Set<String> getGroupNames() {
	return groups.keySet();
    }

    @Deprecated
    public Set<String> getGroups() {
	return groups.keySet();
    }

    public RosterItem getItemByJID(final XmppURI jid) {
	return all.getItem(jid.getJID());
    }

    public Collection<RosterItem> getItems() {
	return new ArrayList<RosterItem>(all.getItems());
    }

    public Collection<RosterItem> getItemsByGroup(final String groupName) {
	final RosterGroup group = getRosterGroup(groupName);
	return group != null ? group.getItems() : null;
    }

    @Override
    public RosterGroup getRosterGroup(final String name) {
	return groups.get(name);
    }

    @Override
    public Collection<RosterGroup> getRosterGroups() {
	return groups.values();
    }

    @Override
    public boolean isRosterReady() {
	return rosterReady;
    }

    @Override
    public void onGroupAdded(final Listener<RosterGroup> listener) {
	onGroupAdded.add(listener);
    }

    @Override
    public void onGroupRemoved(final Listener<RosterGroup> listener) {
	onGroupRemoved.add(listener);
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

    private void addToGroup(final RosterItem item, final String groupName) {
	RosterGroup group = groups.get(groupName);
	if (group == null) {
	    group = addGroup(groupName);
	}
	group.add(item);
    }

    protected RosterGroup addGroup(final String groupName) {
	RosterGroup group;
	group = new RosterGroup(groupName);
	groups.put(groupName, group);
	fireGroupAdded(group);
	return group;
    }

    protected void clearGroupAll() {
	all.clear();
    }

    protected void fireGroupAdded(final RosterGroup group) {
	onGroupAdded.fire(group);
    }

    protected void fireGroupRemoved(final RosterGroup group) {
	onGroupRemoved.fire(group);
    }

    protected void fireItemAdded(final RosterItem item) {
	onItemAdded.fire(item);
    }

    protected void fireItemChanged(final RosterItem item) {
	onItemChanged.fire(item);
	all.fireItemChange(item);
    }

    protected void fireItemRemoved(final RosterItem item) {
	onItemRemoved.fire(item);
    }

    protected void fireRosterReady(final Collection<RosterItem> collection) {
	rosterReady = true;
	onRosterReady.fire(collection);
    }

    protected void removeGroup(final String groupName) {
	final RosterGroup group = groups.remove(groupName);
	if (groupName != null && group != null) {
	    fireGroupRemoved(group);
	}
    }

    protected void removeItem(final RosterItem item) {
	all.remove(item.getJID());
	final ArrayList<String> groupsToRemove = new ArrayList<String>();
	for (final String groupName : getGroupNames()) {
	    final RosterGroup group = getRosterGroup(groupName);
	    group.remove(item.getJID());
	    if (group.getSize() == 0) {
		groupsToRemove.add(groupName);
	    }
	}
	for (final String groupName : groupsToRemove) {
	    removeGroup(groupName);
	}
    }

    protected void storeItem(final RosterItem item) {
	all.add(item);
	addToGroup(item, null);
	for (final String groupName : item.getGroups()) {
	    addToGroup(item, groupName);
	}

    }
}
