/*
 * ((e)) emite: A pure Google Web Toolkit XMPP library
 * Copyright (c) 2008-2011 The Emite development team
 * 
 * This file is part of Emite.
 *
 * Emite is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * Emite is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with Emite.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.calclab.emite.im.client.roster;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import com.calclab.emite.core.client.events.ChangedEvent.ChangeTypes;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.roster.events.RosterGroupChangedEvent;
import com.calclab.emite.im.client.roster.events.RosterItemChangedEvent;

public abstract class XmppRosterGroupsLogic extends XmppRosterBoilerplate {
	protected final HashMap<String, RosterGroup> groups;
	private final RosterGroup all;

	public XmppRosterGroupsLogic(final XmppSession session) {
		super(session);
		groups = new HashMap<String, RosterGroup>();
		all = new RosterGroup(null);
	}

	protected RosterGroup addGroup(final String groupName) {
		RosterGroup group;
		group = groupName != null ? new RosterGroup(groupName) : all;
		groups.put(groupName, group);
		eventBus.fireEvent(new RosterGroupChangedEvent(ChangeTypes.added, group));
		return group;
	}

	protected void addToGroup(final RosterItem item, final String groupName) {
		RosterGroup group = groups.get(groupName);
		if (group == null) {
			group = addGroup(groupName);
		}
		group.add(item);
	}

	protected void clearGroupAll() {
		all.clear();
	}

	protected void fireItemChangedInGroups(final RosterItemChangedEvent event) {
		final XmppURI itemJID = event.getRosterItem().getJID();
		for (final RosterGroup group : groups.values()) {
			if (group.hasItem(itemJID)) {
				group.getRosterGroupEventBus().fireEvent(event);
			}
		}
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

	protected void removeGroup(final String groupName) {
		final RosterGroup group = groups.remove(groupName);
		if (groupName != null && group != null) {
			eventBus.fireEvent(new RosterGroupChangedEvent(ChangeTypes.removed, group));
		}
	}
}
