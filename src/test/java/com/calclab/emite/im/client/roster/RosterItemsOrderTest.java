package com.calclab.emite.im.client.roster;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Comparator;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.client.xmpp.stanzas.Presence.Show;
import com.calclab.emite.xtesting.RosterItemHelper;

public class RosterItemsOrderTest {

    private RosterGroup group;

    @Before
    public void setup() {
	group = new RosterGroup("myGroup");
	group.add(RosterItemHelper.createItem("test3@test", "test3", false, "myGroup"));
	group.add(RosterItemHelper.createItem("test2@test", "test2", true, "myGroup"));
	group.add(RosterItemHelper.createItem("test1@test", "test1", false, "myGroup"));
    }

    @Test
    public void shouldGetUnorderedItems() {
	final ArrayList<RosterItem> list = group.getItemList(null);
	assertNotNull(list);
	assertEquals(3, list.size());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldOrderAvailabiltyAndGroupAndName() {
	final RosterGroup myGroup = new RosterGroup("myGroup");
	myGroup.add(RosterItemHelper.createItem("test2@test", "test2", true, "myGroup"));
	myGroup.add(RosterItemHelper.createItem("test1@test", "test1", false, "other"));
	myGroup.add(RosterItemHelper.createItem("test3@test", "test3", true));
	final Comparator<RosterItem> order = RosterItemsOrder.order(RosterItemsOrder.byAvailability,
		RosterItemsOrder.groupedFirst, RosterItemsOrder.byName);
	final ArrayList<RosterItem> list = myGroup.getItemList(order);
	// available and grouped
	assertEquals("test2", list.get(0).getName());
	// available
	assertEquals("test3", list.get(1).getName());
	// not available
	assertEquals("test1", list.get(2).getName());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldOrderAvailableFirstBusyNext() {
	final RosterItem busy = RosterItemHelper.createItem("test0@test", "test0", true, "myGroup");
	busy.setShow(Show.dnd);
	group.add(busy);
	final ArrayList<RosterItem> list = group.getItemList(RosterItemsOrder.order(RosterItemsOrder.byAvailability,
		RosterItemsOrder.byName));

	// available
	assertEquals("test2", list.get(0).getName());
	// dnd
	assertEquals("test0", list.get(1).getName());
	// not available (alphabetically)
	assertEquals("test1", list.get(2).getName());
	// not available (alphabetically)
	assertEquals("test3", list.get(3).getName());
    }

    @Test
    public void shouldOrderByAvailability() {
	final ArrayList<RosterItem> list = group.getItemList(RosterItemsOrder.byAvailability);
	assertEquals("test2", list.get(0).getName());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldOrderByAvailabilityAndName() {
	final Comparator<RosterItem> order = RosterItemsOrder.order(RosterItemsOrder.byAvailability,
		RosterItemsOrder.byName);
	final ArrayList<RosterItem> list = group.getItemList(order);
	assertEquals("test2", list.get(0).getName());
	assertEquals("test1", list.get(1).getName());
	assertEquals("test3", list.get(2).getName());

    }

    @Test
    public void shouldOrderByGroupedFirst() {
	final RosterGroup myGroup = new RosterGroup("myGroup");
	myGroup.add(RosterItemHelper.createItem("test2@test", "test2", false));
	myGroup.add(RosterItemHelper.createItem("test1@test", "test1", false, "other"));
	myGroup.add(RosterItemHelper.createItem("test3@test", "test3", false, "myGroup"));
	final RosterItem firstItemNotSorted = myGroup.getItemList(null).get(0);
	assertFalse("test3".equals(firstItemNotSorted.getName()));
	final RosterItem firstItemSorted = myGroup.getItemList(RosterItemsOrder.groupedFirst).get(0);
	assertEquals("test3", firstItemSorted.getName());
    }

    @Test
    public void shouldOrderByName() {
	final ArrayList<RosterItem> list = group.getItemList(RosterItemsOrder.byName);
	assertEquals("test1", list.get(0).getName());
	assertEquals("test2", list.get(1).getName());
	assertEquals("test3", list.get(2).getName());
    }
}
