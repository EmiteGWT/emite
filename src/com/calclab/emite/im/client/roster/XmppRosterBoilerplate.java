package com.calclab.emite.im.client.roster;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import com.calclab.emite.core.client.events.EmiteEventBus;
import com.calclab.emite.core.client.events.ChangedEvent.ChangeTypes;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.roster.events.RosterGroupChangedEvent;

public abstract class XmppRosterBoilerplate implements XmppRoster {

    protected final XmppSession session;
    protected EmiteEventBus eventBus;
    protected boolean rosterReady;
    protected HashMap<String, RosterGroup> groups;
    private final RosterGroup all;

    public XmppRosterBoilerplate(XmppSession session) {
	this.session = session;
	this.eventBus = session.getEventBus();
	rosterReady = false;
	groups = new HashMap<String, RosterGroup>();
	all = new RosterGroup(null);
    }

    @Override
    public Set<String> getGroupNames() {
	return groups.keySet();
    }

    @Override
    public RosterItem getItemByJID(final XmppURI jid) {
	return all.getItem(jid.getJID());
    }

    @Override
    public Collection<RosterItem> getItems() {
	return new ArrayList<RosterItem>(all.getItems());
    }

    @Override
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

    protected RosterGroup addGroup(final String groupName) {
	RosterGroup group;
	group = groupName != null ? new RosterGroup(groupName) : all;
	groups.put(groupName, group);
	eventBus.fireEvent(new RosterGroupChangedEvent(ChangeTypes.added, group));
	return group;
    }

    protected void clearGroupAll() {
	all.clear();
    }

    protected void removeGroup(final String groupName) {
	final RosterGroup group = groups.remove(groupName);
	if (groupName != null && group != null) {
	    eventBus.fireEvent(new RosterGroupChangedEvent(ChangeTypes.removed, group));
	}
    }

    protected void storeItem(final RosterItem item) {
	addToGroup(item, null);
	for (final String groupName : item.getGroups()) {
	    addToGroup(item, groupName);
	}
    }

    private void addToGroup(final RosterItem item, final String groupName) {
	RosterGroup group = groups.get(groupName);
	if (group == null) {
	    group = addGroup(groupName);
	}
	group.add(item);
    }
}
