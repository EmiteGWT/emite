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

package com.calclab.emite.im.roster;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.calclab.emite.base.xml.XMLPacket;
import com.calclab.emite.core.IQCallback;
import com.calclab.emite.core.XmppNamespaces;
import com.calclab.emite.core.XmppURI;
import com.calclab.emite.core.events.IQRequestReceivedEvent;
import com.calclab.emite.core.events.PresenceReceivedEvent;
import com.calclab.emite.core.events.RequestFailedEvent;
import com.calclab.emite.core.events.SessionStatusChangedEvent;
import com.calclab.emite.core.events.ChangedEvent.ChangeType;
import com.calclab.emite.core.session.SessionStatus;
import com.calclab.emite.core.session.XmppSession;
import com.calclab.emite.core.stanzas.IQ;
import com.calclab.emite.core.stanzas.Presence;
import com.calclab.emite.im.events.RosterGroupChangedEvent;
import com.calclab.emite.im.events.RosterItemChangedEvent;
import com.calclab.emite.im.events.RosterRetrievedEvent;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;

@Singleton
public final class XmppRosterImpl implements XmppRoster, SessionStatusChangedEvent.Handler, PresenceReceivedEvent.Handler, IQRequestReceivedEvent.Handler {

	private final EventBus eventBus;
	private final XmppSession session;

	private boolean rosterReady = false;
	private final Map<String, RosterGroup> groups;
	private final RosterGroup all;

	@Inject
	protected XmppRosterImpl(@Named("emite") final EventBus eventBus, final XmppSession session) {
		this.eventBus = eventBus;
		this.session = session;

		groups = Maps.newHashMap();
		all = new RosterGroup(eventBus, null);

		session.addSessionStatusChangedHandler(this, true);
		session.addPresenceReceivedHandler(this);
		session.addIQRequestReceivedHandler(this);
	}

	@Override
	public void onSessionStatusChanged(final SessionStatusChangedEvent event) {
		if (SessionStatus.loggedIn.equals(event.getStatus())) {
			reRequestRoster();
		}
	}

	@Override
	public void onPresenceReceived(final PresenceReceivedEvent event) {
		final Presence presence = event.getPresence();
		final RosterItem item = getItemByJID(presence.getFrom());
		if (item != null) {
			final String resource = presence.getFrom().getResource();

			boolean hasChanged = false;

			final boolean wasAvailable = item.getAvailableResources().contains(resource);

			if (presence.getType() == Presence.Type.unavailable) {
				if (wasAvailable) {
					hasChanged = true;
					item.setAvailable(false, resource);
				}
			} else {
				if (!wasAvailable) {
					hasChanged = true;
					item.setAvailable(true, resource);
				}
			}
			
			if (!presence.getShow().equals(item.getShow())) {
				hasChanged = true;
				item.setShow(presence.getShow());
			}

			if (item.getStatus() == null && presence.getStatus() != null || item.getStatus() != null && !item.getStatus().equals(presence.getStatus())) {
				hasChanged = true;
				item.setStatus(presence.getStatus());
			}

			if (hasChanged) {
				final RosterItemChangedEvent changeEvent = new RosterItemChangedEvent(ChangeType.modified, item);
				eventBus.fireEventFromSource(changeEvent, this);

				for (final RosterGroup group : groups.values()) {
					if (group.hasItem(item.getJID())) {
						eventBus.fireEventFromSource(changeEvent, this);
					}
				}
			}
		}
	}

	@Override
	public void onIQRequestReceived(final IQRequestReceivedEvent event) {
		final IQ iq = event.getIQ();
		if (IQ.Type.set.equals(iq.getType())) {
			final XMLPacket query = iq.getQuery(XmppNamespaces.ROSTER);
			if (query != null) {
				for (final XMLPacket child : query.getChildren()) {
					handleItemChanged(RosterItem.parse(child));
				}
			}

			final IQ result = new IQ(IQ.Type.result);
			result.setTo(iq.getFrom());
			result.setId(iq.getId());
			session.send(result);
		}
	}

	@Override
	public HandlerRegistration addRosterGroupChangedHandler(final RosterGroupChangedEvent.Handler handler) {
		return eventBus.addHandlerToSource(RosterGroupChangedEvent.TYPE, this, handler);
	}

	@Override
	public HandlerRegistration addRosterItemChangedHandler(final RosterItemChangedEvent.Handler handler) {
		return eventBus.addHandlerToSource(RosterItemChangedEvent.TYPE, this, handler);
	}

	@Override
	public HandlerRegistration addRosterRetrievedHandler(final RosterRetrievedEvent.Handler handler) {
		return eventBus.addHandlerToSource(RosterRetrievedEvent.TYPE, this, handler);
	}

	private RosterGroup addGroup(final String groupName) {
		final RosterGroup group = groupName != null ? new RosterGroup(eventBus, groupName) : all;
		groups.put(groupName, group);
		eventBus.fireEventFromSource(new RosterGroupChangedEvent(ChangeType.added, group), this);
		return group;
	}

	private void removeGroup(final String groupName) {
		final RosterGroup group = groups.remove(groupName);
		if (groupName != null && group != null) {
			eventBus.fireEventFromSource(new RosterGroupChangedEvent(ChangeType.removed, group), this);
		}
	}

	private void addToGroup(final RosterItem item, final String groupName) {
		RosterGroup group = groups.get(groupName);
		if (group == null) {
			group = addGroup(groupName);
		}
		group.add(item);
	}

	private void clearGroupAll() {
		all.clear();
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
		return Lists.newArrayList(all.getItems());
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

	@Override
	public String getJidName(final XmppURI jid) {
		final RosterItem itemByJID = getItemByJID(jid);
		return itemByJID != null && itemByJID.getName() != null ? itemByJID.getName() : jid.getShortName();
	}

	@Override
	public void requestAddItem(final XmppURI jid, final String name, final String... groups) {
		if (getItemByJID(jid) == null) {
			addOrUpdateItem(jid, name, null, groups);
		}
	}

	@Override
	public void requestRemoveItem(final XmppURI jid) {
		final RosterItem item = getItemByJID(jid.getJID());
		if (item != null) {
			final IQ iq = new IQ(IQ.Type.set);
			final XMLPacket itemNode = iq.addQuery(XmppNamespaces.ROSTER).addChild("item", null);
			itemNode.setAttribute("subscription", "remove");
			itemNode.setAttribute("jid", item.getJID().toString());

			session.sendIQ("roster", iq, new IQCallback() {
				@Override
				public void onIQSuccess(final IQ iq) {
					eventBus.fireEventFromSource(new RequestFailedEvent("rosterItemRemove", "remove roster item failed", iq), this);
				}

				@Override
				public void onIQFailure(final IQ iq) {
				}
			});
		}
	}

	@Override
	public void requestUpdateItem(final RosterItem item) {
		if (getItemByJID(item.getJID()) != null) {
			final IQ iq = new IQ(IQ.Type.set);
			item.addStanzaTo(iq.addQuery(XmppNamespaces.ROSTER));

			session.sendIQ("roster", iq, new IQCallback() {
				@Override
				public void onIQSuccess(final IQ iq) {
					eventBus.fireEventFromSource(new RequestFailedEvent("rosterItemUpdate", "update roster item failed", iq), this);
				}

				@Override
				public void onIQFailure(final IQ iq) {
				}
			});
		}
	}

	@Override
	public void requestUpdateItems(final Collection<RosterItem> items) {
		final IQ iq = new IQ(IQ.Type.set);
		final XMLPacket rosterQuery = iq.addQuery(XmppNamespaces.ROSTER);
		for (final RosterItem item : items) {
			item.addStanzaTo(rosterQuery);
		}

		session.sendIQ("roster", iq, new IQCallback() {
			@Override
			public void onIQSuccess(final IQ iq) {
				eventBus.fireEventFromSource(new RequestFailedEvent("rosterItemsUpdate", "update several roster items failed", iq), this);
			}

			@Override
			public void onIQFailure(final IQ iq) {
			}
		});
	}

	@Override
	public void reRequestRoster() {
		if (session.getCurrentUserURI() != null) {
			final IQ iq = new IQ(IQ.Type.get);
			iq.addQuery(XmppNamespaces.ROSTER);

			session.sendIQ("roster", iq, new IQCallback() {
				@Override
				public void onIQSuccess(final IQ iq) {
					clearGroupAll();

					for (final XMLPacket child : iq.getQuery(XmppNamespaces.ROSTER).getChildren()) {
						final RosterItem item = RosterItem.parse(child);
						storeItem(item);
					}

					if (!rosterReady) {
						rosterReady = true;
						session.setStatus(SessionStatus.rosterReady);
					}
					eventBus.fireEventFromSource(new RosterRetrievedEvent(getItems()), this);
				}

				@Override
				public void onIQFailure(final IQ iq) {
					eventBus.fireEventFromSource(new RequestFailedEvent("roster request", "couldn't retrieve the roster", iq), this);
				}
			});
		}
	}

	private void addOrUpdateItem(final XmppURI jid, final String name, final SubscriptionState subscriptionState, final String... groups) {
		final RosterItem item = new RosterItem(jid, subscriptionState, name, null);
		item.setGroups(groups);
		final IQ iq = new IQ(IQ.Type.set);
		item.addStanzaTo(iq.addQuery(XmppNamespaces.ROSTER));

		session.sendIQ("roster", iq, new IQCallback() {
			@Override
			public void onIQSuccess(final IQ iq) {
				eventBus.fireEventFromSource(new RequestFailedEvent("rosterItem", "roster item can't be updated", iq), this);
			}

			@Override
			public void onIQFailure(final IQ iq) {
			}
		});

	}

	private void handleItemChanged(final RosterItem item) {
		final RosterItem old = getItemByJID(item.getJID());

		if (old == null) { // new item
			storeItem(item);
			eventBus.fireEventFromSource(new RosterItemChangedEvent(ChangeType.added, item), this);
		} else { // update or remove
			final SubscriptionState subscriptionState = item.getSubscriptionState();
			if (subscriptionState == SubscriptionState.remove) {
				removeItem(old);
				eventBus.fireEventFromSource(new RosterItemChangedEvent(ChangeType.removed, old), this);
			} else {
				updateExistingItem(old, item);
				eventBus.fireEventFromSource(new RosterItemChangedEvent(ChangeType.modified, old), this);
			}
		}
	}

	private void removeItem(final RosterItem item) {
		final List<String> groupsToRemove = Lists.newArrayList();
		for (final String groupName : getGroupNames()) {
			final RosterGroup group = getRosterGroup(groupName);
			group.remove(item.getJID());
			if (group.getName() != null && group.getSize() == 0) {
				groupsToRemove.add(groupName);
			}
		}
		for (final String groupName : groupsToRemove) {
			removeGroup(groupName);
		}
	}

	private void updateExistingItem(final RosterItem item, final RosterItem newItem) {
		item.setSubscriptionState(newItem.getSubscriptionState());
		item.setName(newItem.getName());

		final List<String> groups = item.getGroups();
		final List<String> newGroups = newItem.getGroups();

		// Go through and remove any old groups which aren't on the new item
		for (final String group : groups) {
			if (!newGroups.contains(group)) {
				item.removeFromGroup(group);
			}
		}

		// Then go through and add in any new groups which aren't on the
		// existing item
		for (final String group : newGroups) {
			// Update the existing item
			if (!groups.contains(group)) {
				item.addToGroup(group);
			}

			// And update the roster group accordingly
			RosterGroup rosterGroup = XmppRosterImpl.this.getRosterGroup(group);

			if (rosterGroup == null) {
				rosterGroup = addGroup(group);
			}

			if (!rosterGroup.hasItem(item.getJID())) {
				rosterGroup.add(item);
			}
		}

		// And remove the item from any groups it may still be in
		final List<String> groupsToRemove = Lists.newArrayList();

		for (final RosterGroup rosterGroup : getRosterGroups()) {
			if (rosterGroup.getName() != null && !newGroups.contains(rosterGroup.getName()) && rosterGroup.hasItem(item.getJID())) {
				rosterGroup.remove(item.getJID());

				if (rosterGroup.getSize() == 0) {
					groupsToRemove.add(rosterGroup.getName());
				}
			}
		}

		// Remove any groups which are now empty
		for (final String groupName : groupsToRemove) {
			removeGroup(groupName);
		}
	}

	private void storeItem(final RosterItem item) {
		addToGroup(item, null);
		for (final String groupName : item.getGroups()) {
			addToGroup(item, groupName);
		}
	}
}
