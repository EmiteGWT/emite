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
import java.util.List;

import com.calclab.emite.core.client.events.ChangedEvent.ChangeTypes;
import com.calclab.emite.core.client.events.IQEvent;
import com.calclab.emite.core.client.events.IQHandler;
import com.calclab.emite.core.client.events.PresenceEvent;
import com.calclab.emite.core.client.events.PresenceHandler;
import com.calclab.emite.core.client.events.RequestFailedEvent;
import com.calclab.emite.core.client.events.StateChangedEvent;
import com.calclab.emite.core.client.events.StateChangedHandler;
import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.MatcherFactory;
import com.calclab.emite.core.client.packet.NoPacket;
import com.calclab.emite.core.client.packet.PacketMatcher;
import com.calclab.emite.core.client.xmpp.session.IQResponseHandler;
import com.calclab.emite.core.client.xmpp.session.SessionStates;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.calclab.emite.core.client.xmpp.stanzas.IQ;
import com.calclab.emite.core.client.xmpp.stanzas.IQ.Type;
import com.calclab.emite.core.client.xmpp.stanzas.Presence;
import com.calclab.emite.core.client.xmpp.stanzas.Presence.Show;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.roster.events.RosterGroupChangedEvent;
import com.calclab.emite.im.client.roster.events.RosterGroupChangedHandler;
import com.calclab.emite.im.client.roster.events.RosterItemChangedEvent;
import com.calclab.emite.im.client.roster.events.RosterRetrievedEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class XmppRosterLogic extends XmppRosterGroupsLogic {

	private static final PacketMatcher ROSTER_QUERY_FILTER = MatcherFactory.byNameAndXMLNS("query", "jabber:iq:roster");

	@Inject
	public XmppRosterLogic(final XmppSession session) {
		super(session);

		session.addSessionStateChangedHandler(true, new StateChangedHandler() {
			@Override
			public void onStateChanged(final StateChangedEvent event) {
				if (event.is(SessionStates.loggedIn)) {
					reRequestRoster();
				}
			}
		});

		session.addPresenceReceivedHandler(new PresenceHandler() {
			@Override
			public void onPresence(final PresenceEvent event) {
				final Presence presence = event.getPresence();
				final RosterItem item = getItemByJID(presence.getFrom());
				if (item != null) {
					setPresence(presence, item);
				}
			}

			private void setPresence(final Presence presence, final RosterItem item) {
				final Presence.Type type = presence.getType();
				final String resource = presence.getFrom().getResource();

				boolean hasChanged = false;

				final boolean wasAvailable = item.getAvailableResources().contains(resource);

				if (type == Presence.Type.unavailable) {
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
				final Show showReceived = presence.getShow();
				final Show newShow = showReceived == null ? Show.notSpecified : showReceived;

				if (!newShow.equals(item.getShow())) {
					hasChanged = true;
					item.setShow(newShow);
				}

				if (item.getStatus() == null && presence.getStatus() != null || item.getStatus() != null && !item.getStatus().equals(presence.getStatus())) {
					hasChanged = true;
					item.setStatus(presence.getStatus());
				}

				if (hasChanged) {
					final RosterItemChangedEvent event = new RosterItemChangedEvent(ChangeTypes.modified, item);
					eventBus.fireEvent(event);
					fireItemChangedInGroups(event);
				}
			}

		});

		session.addIQReceivedHandler(new IQHandler() {
			@Override
			public void onPacket(final IQEvent event) {
				final IQ iq = event.getIQ();
				if (iq.isType(IQ.Type.set)) {
					final IPacket query = iq.getFirstChild(ROSTER_QUERY_FILTER);
					if (query != NoPacket.INSTANCE) {
						for (final IPacket child : query.getChildren()) {
							handleItemChanged(RosterItem.parse(child));
						}
					}
					session.send(new IQ(Type.result).With("to", iq.getFromAsString()).With("id", iq.getId()));
				}
			}
		});
	}

	@Override
	public HandlerRegistration addRosterGroupChangedHandler(final RosterGroupChangedHandler handler) {
		return RosterGroupChangedEvent.bind(eventBus, handler);
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
			final IQ iq = new IQ(Type.set);
			final IPacket itemNode = iq.addQuery("jabber:iq:roster").addChild("item", null);
			itemNode.With("subscription", "remove").With("jid", item.getJID().toString());

			session.sendIQ("roster", iq, new IQResponseHandler() {
				@Override
				public void onIQ(final IQ iq) {
					if (!IQ.isSuccess(iq)) {
						eventBus.fireEvent(new RequestFailedEvent("rosterItemRemove", "remove roster item failed", iq));
					}
				}
			});
		}
	}

	@Override
	public void requestUpdateItem(final RosterItem item) {
		if (getItemByJID(item.getJID()) != null) {
			final IQ iq = new IQ(Type.set);
			item.addStanzaTo(iq.addQuery("jabber:iq:roster"));

			session.sendIQ("roster", iq, new IQResponseHandler() {
				@Override
				public void onIQ(final IQ iq) {
					if (!IQ.isSuccess(iq)) {
						eventBus.fireEvent(new RequestFailedEvent("rosterItemUpdate", "update roster item failed", iq));
					}
				}
			});
		}
	}

	@Override
	public void requestUpdateItems(final Collection<RosterItem> items) {
		final IQ iq = new IQ(Type.set);
		final IPacket rosterQuery = iq.addQuery("jabber:iq:roster");
		for (final RosterItem item : items) {
			item.addStanzaTo(rosterQuery);
		}

		session.sendIQ("roster", iq, new IQResponseHandler() {
			@Override
			public void onIQ(final IQ iq) {
				if (!IQ.isSuccess(iq)) {
					eventBus.fireEvent(new RequestFailedEvent("rosterItemsUpdate", "update several roster items failed", iq));
				}
			}
		});
	}

	@Override
	public void reRequestRoster() {
		if (session.getCurrentUserURI() != null) {
			final IQ iq = new IQ(IQ.Type.get, null).WithQuery("jabber:iq:roster");
			session.sendIQ("roster", iq, new IQResponseHandler() {
				@Override
				public void onIQ(final IQ iq) {
					if (IQ.isSuccess(iq)) {
						clearGroupAll();
						final List<? extends IPacket> children = iq.getFirstChild("query").getChildren();

						for (final IPacket child : children) {
							final RosterItem item = RosterItem.parse(child);
							storeItem(item);
						}

						if (!rosterReady) {
							rosterReady = true;
							session.setSessionState(SessionStates.rosterReady);
						}
						eventBus.fireEvent(new RosterRetrievedEvent(getItems()));
					} else {
						eventBus.fireEvent(new RequestFailedEvent("roster request", "couldn't retrieve the roster", iq));
					}
				}
			});
		}
	}

	private void addOrUpdateItem(final XmppURI jid, final String name, final SubscriptionState subscriptionState, final String... groups) {
		final RosterItem item = new RosterItem(jid, subscriptionState, name, null);
		item.setGroups(groups);
		final IQ iq = new IQ(Type.set);
		item.addStanzaTo(iq.addQuery("jabber:iq:roster"));

		session.sendIQ("roster", iq, new IQResponseHandler() {
			@Override
			public void onIQ(final IQ iq) {
				if (!IQ.isSuccess(iq)) {
					eventBus.fireEvent(new RequestFailedEvent("rosterItem", "roster item can't be updated", iq));
				}
			}
		});

	}

	private void handleItemChanged(final RosterItem item) {
		final RosterItem old = getItemByJID(item.getJID());

		if (old == null) { // new item
			storeItem(item);
			eventBus.fireEvent(new RosterItemChangedEvent(ChangeTypes.added, item));
		} else { // update or remove
			final SubscriptionState subscriptionState = item.getSubscriptionState();
			if (subscriptionState == SubscriptionState.remove) {
				removeItem(old);
				eventBus.fireEvent(new RosterItemChangedEvent(ChangeTypes.removed, old));
			} else {
				updateExistingItem(old, item);
				eventBus.fireEvent(new RosterItemChangedEvent(ChangeTypes.modified, old));
			}
		}
	}

	private void removeItem(final RosterItem item) {
		final ArrayList<String> groupsToRemove = new ArrayList<String>();
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
			RosterGroup rosterGroup = XmppRosterLogic.this.getRosterGroup(group);

			if (rosterGroup == null) {
				rosterGroup = addGroup(group);
			}

			if (!rosterGroup.hasItem(item.getJID())) {
				rosterGroup.add(item);
			}
		}

		// And remove the item from any groups it may still be in
		final ArrayList<String> groupsToRemove = new ArrayList<String>();

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

	void storeItem(final RosterItem item) {
		addToGroup(item, null);
		for (final String groupName : item.getGroups()) {
			addToGroup(item, groupName);
		}
	}
}
