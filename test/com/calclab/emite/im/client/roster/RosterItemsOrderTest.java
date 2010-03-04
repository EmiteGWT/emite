package com.calclab.emite.im.client.roster;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Comparator;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.xtesting.RosterTester;

public class RosterItemsOrderTest {

    private RosterGroup group;

    @Before
    public void setup() {
	group = new RosterGroup("myGroup");
	group.add(RosterTester.createItem("test3@test", "test3", false, "myGroup"));
	group.add(RosterTester.createItem("test2@test", "test2", true, "myGroup"));
	group.add(RosterTester.createItem("test1@test", "test1", false, "myGroup"));
    }

    @Test
    public void shouldGetUnorderedItems() {
	final ArrayList<RosterItem> list = group.getItemList(null);
	assertNotNull(list);
	assertEquals(3, list.size());
    }

    @Test
    public void shouldOrderByAvailability() {
	final ArrayList<RosterItem> list = group.getItemList(RosterItemsOrder.byAvailability);
	assertEquals("test2", list.get(0).getName());
    }

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
    public void shouldOrderByAvailabilityGroupsAndName() {
    }

    @Test
    public void shouldOrderByGroupAvailabiltyAndName() {
	final RosterGroup myGroup = new RosterGroup("myGroup");
	myGroup.add(RosterTester.createItem("test2@test", "test2", true, "myGroup"));
	myGroup.add(RosterTester.createItem("test1@test", "test1", false, "other"));
	myGroup.add(RosterTester.createItem("test3@test", "test3", true));
	final Comparator<RosterItem> order = RosterItemsOrder.order(RosterItemsOrder.byAvailability,
		RosterItemsOrder.groupedFirst, RosterItemsOrder.byName);
	final ArrayList<RosterItem> list = myGroup.getItemList(order);
	assertEquals("test3", list.get(0).getName());
	assertEquals("test2", list.get(1).getName());
	assertEquals("test1", list.get(2).getName());

    }

    @Test
    public void shouldOrderByName() {
	final ArrayList<RosterItem> list = group.getItemList(RosterItemsOrder.byName);
	assertEquals("test1", list.get(0).getName());
	assertEquals("test2", list.get(1).getName());
	assertEquals("test3", list.get(2).getName());
    }

    @Test
    public void shouldOrderByNonGroupedFirst() {
	final RosterGroup myGroup = new RosterGroup("myGroup");
	myGroup.add(RosterTester.createItem("test2@test", "test2", true, "myGroup"));
	myGroup.add(RosterTester.createItem("test1@test", "test1", false, "other"));
	myGroup.add(RosterTester.createItem("test3@test", "test3", false));
	final RosterItem firstItemNotSorted = myGroup.getItemList(null).get(0);
	assertFalse("test3".equals(firstItemNotSorted.getName()));
	final RosterItem firstItemSorted = myGroup.getItemList(RosterItemsOrder.groupedFirst).get(0);
	assertEquals("test3", firstItemSorted.getName());
    }
}
