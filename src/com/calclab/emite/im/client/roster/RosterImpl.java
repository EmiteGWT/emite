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
import com.calclab.emite.im.client.roster.events.RosterGroupChangedEvent;
import com.calclab.emite.im.client.roster.events.RosterGroupChangedHandler;
import com.calclab.emite.im.client.roster.events.RosterItemChangedEvent;
import com.calclab.emite.im.client.roster.events.RosterItemChangedHandler;
import com.calclab.emite.im.client.roster.events.RosterRetrievedEvent;
import com.calclab.emite.im.client.roster.events.RosterRetrievedHandler;
import com.calclab.suco.client.events.Listener;
import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Roster Xmpp implementation.
 * 
 * @see Roster
 */
@Singleton
public class RosterImpl implements Roster {

    private final XmppRoster delegate;

    @Inject
    public RosterImpl(XmppRoster delegate) {
	GWT.log("Creating RosterImpl");
	this.delegate = delegate;
    }

    @Override
    public void addItem(XmppURI jid, String name, String... groups) {
	delegate.requestAddItem(jid, name, groups);
    }

    @Override
    public Set<String> getGroupNames() {
	return delegate.getGroupNames();
    }

    @Override
    public Set<String> getGroups() {
	return delegate.getGroupNames();
    }

    @Override
    public RosterItem getItemByJID(XmppURI jid) {
	return delegate.getItemByJID(jid);
    }

    @Override
    public Collection<RosterItem> getItems() {
	return delegate.getItems();
    }

    @Override
    public Collection<RosterItem> getItemsByGroup(String groupName) {
	return delegate.getItemsByGroup(groupName);
    }

    @Override
    public RosterGroup getRosterGroup(String name) {
	return delegate.getRosterGroup(name);
    }

    @Override
    public Collection<RosterGroup> getRosterGroups() {
	return delegate.getRosterGroups();
    }

    @Override
    public boolean isRosterReady() {
	return delegate.isRosterReady();
    }

    @Override
    public void onGroupAdded(final Listener<RosterGroup> listener) {
	delegate.addRosterGroupChangedHandler(new RosterGroupChangedHandler() {
	    @Override
	    public void onGroupChanged(RosterGroupChangedEvent event) {
		if (event.isAdded()) {
		    listener.onEvent(event.getRosterGroup());
		}
	    }
	});
    }

    @Override
    public void onGroupRemoved(final Listener<RosterGroup> listener) {
	delegate.addRosterGroupChangedHandler(new RosterGroupChangedHandler() {
	    @Override
	    public void onGroupChanged(RosterGroupChangedEvent event) {
		if (event.isRemoved()) {
		    listener.onEvent(event.getRosterGroup());
		}
	    }
	});

    }

    @Override
    public void onItemAdded(final Listener<RosterItem> listener) {
	delegate.addRosterItemChangedHandler(new RosterItemChangedHandler() {
	    @Override
	    public void onRosterItemChanged(RosterItemChangedEvent event) {
		if (event.isAdded()) {
		    listener.onEvent(event.getRosterItem());
		}
	    }
	});
    }

    @Override
    public void onItemChanged(final Listener<RosterItem> listener) {
	delegate.addRosterItemChangedHandler(new RosterItemChangedHandler() {
	    @Override
	    public void onRosterItemChanged(RosterItemChangedEvent event) {
		if (event.isModified()) {
		    listener.onEvent(event.getRosterItem());
		}
	    }
	});
    }

    @Override
    public void onItemRemoved(final Listener<RosterItem> listener) {
	delegate.addRosterItemChangedHandler(new RosterItemChangedHandler() {
	    @Override
	    public void onRosterItemChanged(RosterItemChangedEvent event) {
		if (event.isRemoved()) {
		    listener.onEvent(event.getRosterItem());
		}
	    }
	});
    }

    @Override
    public void onItemUpdated(Listener<RosterItem> listener) {
	onItemChanged(listener);
    }

    @Override
    public void onRosterRetrieved(final Listener<Collection<RosterItem>> listener) {
	delegate.addRosterRetrievedHandler(new RosterRetrievedHandler() {
	    @Override
	    public void onRosterRetrieved(RosterRetrievedEvent event) {
		listener.onEvent(event.getRosterItems());
	    }
	});
    }

    @Override
    public void removeItem(XmppURI jid) {
	delegate.requestRemoveItem(jid);
    }

    @Override
    public void requestAddItem(XmppURI jid, String name, String... groups) {
	delegate.requestAddItem(jid, name, groups);
    }

    @Override
    public void requestRemoveItem(XmppURI jid) {
	delegate.requestRemoveItem(jid);
    }

    @Override
    public void requestUpdateItem(RosterItem item) {
	delegate.requestUpdateItem(item);
    }

    @Override
    public void requestUpdateItems(Collection<RosterItem> items) {
	delegate.requestUpdateItems(items);
    }

    @Override
    public void updateItem(XmppURI jid, String name, String... groups) {
	final RosterItem oldItem = getItemByJID(jid);
	if (oldItem != null) {
	    oldItem.setName(name);
	    oldItem.setGroups(groups);
	    requestUpdateItem(oldItem);
	}
    }

}
