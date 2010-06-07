package com.calclab.emite.im.client.roster;

import static org.junit.Assert.assertEquals;

import java.util.Comparator;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.client.xmpp.stanzas.Presence.Show;
import com.calclab.emite.xtesting.RosterTester;

public class OrderByAvailabilityTests {

    private RosterItem chat;
    private RosterItem dnd;
    private RosterItem away;
    private RosterItem xa;
    private Comparator<RosterItem> comparator;
    private RosterItem available;
    private RosterItem unavailable;

    @Before
    public void setupItems() {
	comparator = RosterItemsOrder.byAvailability;

	available = RosterTester.createItem("unknown@test", "unknown", true, "group");
	chat = RosterTester.createItem("chat@test", "chat", true, "group");
	chat.setShow(Show.chat);
	dnd = RosterTester.createItem("dnd@test", "dnd", true, "group");
	dnd.setShow(Show.dnd);
	away = RosterTester.createItem("away@test", "away", true, "group");
	away.setShow(Show.away);
	xa = RosterTester.createItem("away@test", "away", true, "group");
	xa.setShow(Show.xa);

	unavailable = RosterTester.createItem("unavailable@test", "unavailable", false, "group");
    }

    @Test
    public void shouldSortAvailableFirst() {
	assertSamePosition(chat, available);
	assertFirstItemFirst(chat, away);
	assertFirstItemFirst(chat, dnd);
	assertFirstItemFirst(chat, xa);
	assertFirstItemFirst(chat, unavailable);
    }

    @Test
    public void shouldSortAwayAndXaAfterAvailable() {
	assertSecondItemFirst(away, available);
	assertSecondItemFirst(away, chat);
	assertSamePosition(away, xa);
	assertFirstItemFirst(away, dnd);
	assertFirstItemFirst(away, unavailable);
    }

    @Test
    public void shouldSortDndAfterAway() {
	assertSecondItemFirst(dnd, available);
	assertSecondItemFirst(dnd, chat);
	assertSecondItemFirst(dnd, away);
	assertSecondItemFirst(dnd, xa);
	assertFirstItemFirst(dnd, unavailable);
    }

    private void assertFirstItemFirst(final RosterItem item1, final RosterItem item2) {
	Assert.assertTrue(comparator.compare(item1, item2) < 0);
    }

    private void assertSamePosition(final RosterItem item1, final RosterItem item2) {
	assertEquals(0, comparator.compare(item1, item2));
    }

    private void assertSecondItemFirst(final RosterItem item1, final RosterItem item2) {
	Assert.assertTrue(comparator.compare(item1, item2) > 0);
    }
}
