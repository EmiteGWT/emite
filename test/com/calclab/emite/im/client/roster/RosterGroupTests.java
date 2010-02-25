package com.calclab.emite.im.client.roster;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.xtesting.RosterTester;
import com.calclab.emite.xtesting.SessionTester;
import com.calclab.suco.testing.events.MockedListener;

public class RosterGroupTests {

    private XmppRoster roster;

    @Before
    public void setup() {
	roster = new XmppRoster(new SessionTester());
    }

    @Test
    public void shouldFireItemChanged() {
	final RosterItem item = RosterTester.createItem("test1@dom", "test1", "A", "B");
	roster.storeItem(item);
	final MockedListener<RosterItem> itemChangeListener = new MockedListener<RosterItem>();
	for (final String name : roster.getGroupNames()) {
	    roster.getRosterGroup(name).onItemChanged(itemChangeListener);
	}
	roster.fireItemChanged(item);
	assertEquals(3, itemChangeListener.getCalledTimes());
    }

    @Test
    public void shouldHaveCreateGroupsWhenStoreItems() {
	final MockedListener<RosterGroup> groupAddedListener = new MockedListener<RosterGroup>();
	roster.onGroupAdded(groupAddedListener);
	roster.storeItem(RosterTester.createItem("test1@dom", "test1", "A", "B"));
	roster.storeItem(RosterTester.createItem("test2@dom", "test2", "A"));
	roster.storeItem(RosterTester.createItem("test3@dom", "test3"));
	assertEquals(3, groupAddedListener.getCalledTimes());

	final RosterGroup all = roster.getRosterGroup(null);
	assertNotNull(all);
	assertEquals(3, all.getSize());

	assertNotNull(roster.getRosterGroup("A"));
	assertEquals(2, roster.getRosterGroup("A").getSize());
	assertNotNull(roster.getRosterGroup("B"));
	assertEquals(1, roster.getRosterGroup("B").getSize());
    }

}
